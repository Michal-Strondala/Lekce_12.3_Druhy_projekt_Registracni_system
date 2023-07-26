package com.engeto.project2.project2_story.services;

import com.engeto.project2.project2_story.model.User;

import java.util.List;

public interface UserRepository {
//    založení nového uživatele
    int addUser(User user);


//    informace o uživateli
    User selectOneUserByID(Long ID);


//    informace o všech uživatelích
    List<User> selectAllUsers();


//    upravit informace o uživateli
    int updateUser(User user);


//    smazat uživatele
    int deleteUserByID(Long ID);

    // najít uživatele podle jména
    List<User> findByNameContaining(String name);
}
