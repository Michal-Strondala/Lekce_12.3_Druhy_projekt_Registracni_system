package com.engeto.project2.project2_story.services;

import com.engeto.project2.project2_story.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.engeto.project2.project2_story.model.UserNonDetailedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // region Založení nového uživatele
    // UUID je generováno pomocí SQL funkce UUID_TO_BIN(UUID()) a ID je generováno a incrementováno automaticky
    @Override
    public int createUser(User user) {
       List<String> validPersonIDs = readValidPersonIDsFromFile();

       for (String personID : validPersonIDs) {
           if (personID.length() != 12)
               continue; // délka PersonID není validní, přeskoč na další krok
           if (isPersonIDInDatabase(personID))
               continue; // toto PersonID je již v databázi, přeskoč na další krok
           return jdbcTemplate.update("INSERT INTO UsersTable (name, surname, personID, uuid) VALUES(?,?,?,UUID_TO_BIN(UUID()))",
                user.getName(), user.getSurname(), personID);
       }
        return 0; // Nebylo nalezeno žádné validní PersonID nebo jsou již všechny v databázi
    }

    // Načtení personID ze souboru personID.txt
    private List<String> readValidPersonIDsFromFile() {
        List<String> validPersonIDs = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File("src/main/resources/personID.txt"));
            while (scanner.hasNextLine()) {
                String personID = scanner.nextLine().trim();
                validPersonIDs.add(personID);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return validPersonIDs;
    }

    // kontrola, jestli není personID v databázi
    private boolean isPersonIDInDatabase(String personID) {
        String sql = "SELECT COUNT(*) FROM UsersTable WHERE personID=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, personID);
        return count != null && count > 0;
    }
    // endregion


    // region Informace o uživateli
    @Override
    public Object selectUserByID(Long ID, boolean detail) {
        try {
            String sql;
            // pokud chceme vypsat detailní info o uživateli (tzn. jméno, přijmení, personID a UUID)
            if (detail) {
                sql = "SELECT * FROM UsersTable WHERE ID=?";
                User user = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(User.class), ID);
                return user;
            // pokud chceme vypsat základní info o uživateli (tzn. pouze jméno a přijmení)
            } else {
                sql = "SELECT ID, name, surname FROM UsersTable WHERE ID=?";
                UserNonDetailedResponse userNonDetailedResponse = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(UserNonDetailedResponse.class), ID);
                return userNonDetailedResponse;
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }
    // endregion


    // region Informace o všech uživatelích
    @Override
    public List<Object> selectAllUsers(boolean detail) {
        String sql;
        // pokud chceme vypsat detailní info o všech uživatelích (tzn. jméno, přijmení, personID a UUID)
        if (detail) {
            sql = "SELECT * FROM UsersTable";
            List<User> users = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class));
            return new ArrayList<>(users);
        // pokud chceme vypsat základní info o všech uživatelích (tzn. pouze jméno a přijmení)
        } else {
            sql = "SELECT ID, name, surname FROM UsersTable";
            List<UserNonDetailedResponse> usersNonDetailed = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(UserNonDetailedResponse.class));
            return new ArrayList<>(usersNonDetailed);
        }
    }
    // endregion


    // region Upravit informace o uživateli
    @Override
    public int updateUser(Object userObject) {
        if (userObject instanceof User user) {
            return jdbcTemplate.update("UPDATE UsersTable SET name = ?, surname = ? WHERE ID = ?",
                    user.getName(), user.getSurname(), user.getID());
        } else if (userObject instanceof UserNonDetailedResponse userNonDetailedResponse) {
            return jdbcTemplate.update("UPDATE UsersTable SET name = ?, surname = ? WHERE ID = ?",
                userNonDetailedResponse.getName(), userNonDetailedResponse.getSurname(), userNonDetailedResponse.getID());
        } else {
            return 0;
        }
    }
    // endregion


    // region Smazat uživatele podle jeho ID v tabulce
    @Override
    public int deleteUserByID(Long ID) {
        return jdbcTemplate.update("DELETE FROM UsersTable WHERE ID=?", ID);
    }
    // endregion


    // region Najít uživatele podle jména
    @Override
    public List<Object> findByNameContaining(String name, boolean detail) {
        String sql;
        if (detail) {
            sql = "SELECT * FROM UsersTable WHERE name LIKE '%" + name + "%'";
            List<User> usersByName = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class));
            return new ArrayList<>(usersByName);
        } else {
            sql = "SELECT ID, name, surname FROM UsersTable WHERE name LIKE '%" + name + "%'";
            List<UserNonDetailedResponse> userNonDetailedResponseByName = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(UserNonDetailedResponse.class));
            return new ArrayList<>(userNonDetailedResponseByName);
        }
    }
    // endregion
}
