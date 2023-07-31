package com.engeto.project2.project2_story.controllers;

import com.engeto.project2.project2_story.model.User;
import com.engeto.project2.project2_story.model.UserNonDetailed;
import com.engeto.project2.project2_story.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MainController {
    @Autowired
    UserRepository userRepository;

    // kontrola, jestli aplikace funguje
    @GetMapping("/check")
    public String check() {
        return "Welcome to registration system";
    }

//    založení nového uživatele
    // personID musí být ověřeno, že je validní. Zde máš k dispozici soubor, ve kterém jsou po řádcích napsané validní personID
    // Dále je nutné ověřit, zda již uživatel s personID není v databázi.
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userRepository.createUser(new User(user.getName(), user.getSurname(), user.getPersonID()));
            return new ResponseEntity<>("User was created successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    informace o uživateli

    // ještě existuje varianta api/v1/users/{ID}?detail=true
    // Kdy request vrátí rozšířený objekt:
    // {id: string, name: string, surname: string, personID: string , uuid: string
    @GetMapping("/user/{ID}")
    public ResponseEntity<Object> getUserById(@PathVariable("ID") Long ID, @RequestParam(required = false) boolean detail) {
        Object user = userRepository.selectUserByID(ID, detail);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //    informace o všech uživatelích
    // ještě existuje varianta api/v1/users?detail=true
    // Kdy request vrátí rozšířený objekt:
    // {id: string, name: string, surname: string, personID: string , uuid: string
    @GetMapping("/users")
    public ResponseEntity<List<Object>> getAllUsers(@RequestParam(required = false) String name, boolean detail) {
        try {
            List<Object> users = new ArrayList<>();

            if (name == null)
                users.addAll(userRepository.selectAllUsers(detail));
            else
                users.addAll(userRepository.findByNameContaining(name, detail));

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    upravit informace o uživateli
    // Z toho tedy je jasné, že je možné upravit pouze name a surname
    @PutMapping("/user/{ID}")
    public ResponseEntity<String> updateUser(@PathVariable("ID") Long ID, @RequestBody User user, @RequestParam(required = false) boolean detail) {
        Object userToUpdateObject = userRepository.selectUserByID(ID, detail);

        if (userToUpdateObject instanceof User userToUpdate) {
            userToUpdate.setName(user.getName());
            userToUpdate.setSurname(user.getSurname());

            userRepository.updateUser(userToUpdate);
            return new ResponseEntity<>("User was updated successfully.", HttpStatus.OK);
        } else if (userToUpdateObject instanceof UserNonDetailed userNonDetailedToUpdate) {
            userNonDetailedToUpdate.setName(user.getName());
            userNonDetailedToUpdate.setSurname(user.getSurname());

            userRepository.updateUser(userNonDetailedToUpdate);
            return new ResponseEntity<>("User was updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot find user with ID=" + ID, HttpStatus.NOT_FOUND);
        }
    }


    //    smazat uživatele
    @DeleteMapping("/user/{ID}")
    public ResponseEntity<String> deleteUser(@PathVariable("ID") Long ID) {
        try {
            int result = userRepository.deleteUserByID(ID);
            if (result == 0) {
                return new ResponseEntity<>("Cannot find user with ID=" + ID, HttpStatus.OK);
            }
            return new ResponseEntity<>("User was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// dodělat detaily ze zadání

}
