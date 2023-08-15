package com.engeto.project2.project2_story.controllers;

import com.engeto.project2.project2_story.model.User;
import com.engeto.project2.project2_story.model.UserNonDetailedResponse;
import com.engeto.project2.project2_story.model.Views;
import com.engeto.project2.project2_story.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MainController {
    // Druhý projekt Genesis Resources - Registrační systém
    // Michal Střondala - na Discordu Michal Stř.

    @Autowired
    UserService userService;


    // region Kontrola, jestli aplikace funguje
    @GetMapping("/check")
    public String check() {
        return "Welcome to registration system";
    }
    // endregion


    // region Založení nového uživatele
    // personID musí být ověřeno, že je validní. Zde máš k dispozici soubor, ve kterém jsou po řádcích napsané validní personID
    // Dále je nutné ověřit, zda již uživatel s personID není v databázi.
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        int result = userService.createUser(user);

        if (result == 1) {
            return new ResponseEntity<>("User was created successfully.", HttpStatus.CREATED);
        } else if (result == 0) {
            return new ResponseEntity<>("No valid personID found or already in the database.", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // endregion


    // region Informace o uživateli
    // existuje varianta api/v1/users/{ID}?detail=true
    // Kdy request vrátí rozšířený objekt:
    // {id: string, name: string, surname: string, personID: string , uuid: string}
    @GetMapping("/user/{ID}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Object> getUserById(@PathVariable("ID") Long ID, @RequestParam(required = false) boolean detail) {
        Object user = userService.selectUserByID(ID, detail);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // endregion


    // region Informace o všech uživatelích
    // existuje varianta api/v1/users?detail=true
    // Kdy request vrátí rozšířený objekt:
    // {id: string, name: string, surname: string, personID: string , uuid: string}
    @GetMapping("/users")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Object>> getAllUsers(@RequestParam(required = false) String name, boolean detail) {
        try {
            List<Object> users = new ArrayList<>();

            if (name == null)
                users.addAll(userService.selectAllUsers(detail));
            else
                users.addAll(userService.findByNameContaining(name, detail));

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // endregion


    // region Upravit informace o uživateli
    // je možné upravit pouze name a surname
    @PutMapping("/user/{ID}")
    public ResponseEntity<String> updateUser(@PathVariable("ID") Long ID, @RequestBody User user, @RequestParam(required = false) boolean detail) {
        Object userToUpdateObject = userService.selectUserByID(ID, detail);

        if (userToUpdateObject instanceof User userToUpdate) {
            userToUpdate.setName(user.getName());
            userToUpdate.setSurname(user.getSurname());

            userService.updateUser(userToUpdate);
            return new ResponseEntity<>("User was updated successfully.", HttpStatus.OK);
        } else if (userToUpdateObject instanceof UserNonDetailedResponse userNonDetailedResponseToUpdate) {
            userNonDetailedResponseToUpdate.setName(user.getName());
            userNonDetailedResponseToUpdate.setSurname(user.getSurname());

            userService.updateUser(userNonDetailedResponseToUpdate);
            return new ResponseEntity<>("User was updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot find user with ID=" + ID, HttpStatus.NOT_FOUND);
        }
    }
    // endregion


    // region Smazat uživatele
    @DeleteMapping("/user/{ID}")
    public ResponseEntity<String> deleteUser(@PathVariable("ID") Long ID) {
        try {
            int result = userService.deleteUserByID(ID);
            if (result == 0) {
                return new ResponseEntity<>("Cannot find user with ID=" + ID, HttpStatus.OK);
            }
            return new ResponseEntity<>("User was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // endregion
}
