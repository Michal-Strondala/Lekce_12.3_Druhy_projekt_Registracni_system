package com.engeto.project2.project2_story.services;

import com.engeto.project2.project2_story.model.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final List<User> users = new ArrayList<>();

    //    založení nového uživatele
    @Override
    public int addUser(User user) {
        return jdbcTemplate.update("INSERT INTO UsersTable (name, surname, personID, uuid) VALUES(?,?,?,?)",
                user.getName(), user.getSurname(), user.getPersonID(), user.getUuid());
    }



    //    informace o uživateli
    @Override
    public User selectOneUserByID(Long ID) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM UsersTable WHERE ID=?",
                    BeanPropertyRowMapper.newInstance(User.class), ID);

            return user;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    //    informace o všech uživatelích
    @Override
    public List<User> selectAllUsers() {
        return jdbcTemplate.query("SELECT * from UsersTable", BeanPropertyRowMapper.newInstance(User.class));
    }

    //    upravit informace o uživateli
    @Override
    public int updateUser(User user) {
        return jdbcTemplate.update("UPDATE UsersTable SET name=?, surname=?, personID=?, uuid=? WHERE ID=?",
                user.getName(), user.getSurname(), user.getPersonID(), user.getUuid(), user.getID());
    }

    //    smazat uživatele
    @Override
    public int deleteUserByID(Long ID) {
        return jdbcTemplate.update("DELETE FROM UsersTable WHERE ID=?", ID);
    }

    @Override
    public List<User> findByNameContaining(String name) {
        String s = "SELECT * FROM UsersTable WHERE name LIKE '%" + name + "%'";

        return jdbcTemplate.query(s, BeanPropertyRowMapper.newInstance(User.class));
    }
}
