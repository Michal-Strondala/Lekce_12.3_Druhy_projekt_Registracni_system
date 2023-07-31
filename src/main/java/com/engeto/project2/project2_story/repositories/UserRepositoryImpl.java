package com.engeto.project2.project2_story.repositories;

import com.engeto.project2.project2_story.model.User;
import java.util.ArrayList;
import java.util.List;

import com.engeto.project2.project2_story.model.UserNonDetailed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //    založení nového uživatele
    // UUID je generováno pomocí SQL funkce UUID_TO_BIN(UUID()) a ID je generováno a incrementováno automaticky
    @Override
    public int createUser(User user) {
        return jdbcTemplate.update("INSERT INTO UsersTable (name, surname, personID, uuid) VALUES(?,?,?,UUID_TO_BIN(UUID()))",
                user.getName(), user.getSurname(), user.getPersonID());
    }

    //    informace o uživateli
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
                UserNonDetailed userNonDetailed = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(UserNonDetailed.class), ID);
                return userNonDetailed;
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    //    informace o všech uživatelích
    @Override
    public List<Object> selectAllUsers(boolean detail) {
        String sql;
        // pokud chceme vypsat detailní info o všech uživatelích (tzn. jméno, přijmení, personID a UUID)
        if (detail) {
            sql = "SELECT * from UsersTable";
            List<User> users = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class));
            return new ArrayList<>(users);
            // pokud chceme vypsat základní info o všech uživatelích (tzn. pouze jméno a přijmení)
        } else {
            sql = "SELECT ID, name, surname from UsersTable";
            List<UserNonDetailed> usersNonDetailed = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(UserNonDetailed.class));
            return new ArrayList<>(usersNonDetailed);
        }
    }

    //    upravit informace o uživateli
    @Override
    public int updateUser(Object userObject) {
        if (userObject instanceof User user) {
            return jdbcTemplate.update("UPDATE UsersTable SET name = ?, surname = ? WHERE ID = ?",
                    user.getName(), user.getSurname(), user.getID());
        } else if (userObject instanceof UserNonDetailed userNonDetailed) {
            return jdbcTemplate.update("UPDATE UsersTable SET name = ?, surname = ? WHERE ID = ?",
                userNonDetailed.getName(), userNonDetailed.getSurname(), userNonDetailed.getID());
        } else {
            return 0;
        }
    }

    //    smazat uživatele podle jeho ID v tabulce
    @Override
    public int deleteUserByID(Long ID) {
        return jdbcTemplate.update("DELETE FROM UsersTable WHERE ID=?", ID);
    }

    @Override
    public List<User> findByNameContaining(String name, boolean detail) {
        String sql;
        if (detail) {
            sql = "SELECT * FROM UsersTable WHERE name LIKE '%" + name + "%'";
        } else {
            sql = "SELECT ID, name, surname FROM UsersTable WHERE name LIKE '%" + name + "%'";
        }

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class));
    }
}
