package com.engeto.project2.project2_story.controllers;

import com.engeto.project2.project2_story.model.User;
import com.engeto.project2.project2_story.services.UserRepository;
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

//    založení nového uživatele
    // personID musí být ověřeno, že je validní. Zde máš k dispozici soubor, ve kterém jsou po řádcích napsané validní personID
    // Dále je nutné ověřit, zda již uživatel s personID není v databázi.


    // UUID - Generování
    // Pro každý nový záznam musí být vygenerovaný ještě UUID, což bude další jedinečný identifikátor uživatele.
    // Na vygenerování můžeš použít například následující knihovnu (https://www.baeldung.com/java-uuid).
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userRepository.addUser(new User(user.getName(), user.getSurname(), user.getPersonID(), user.getUuid()));
            return new ResponseEntity<>("User was created successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    informace o uživateli

    //ještě existuje varianta api/v1/users/{ID}?detail=true
    //Kdy request vrátí rozšířený objekt:
    //
    //Ukázka kódu3
    //{id: string, name: string, surname: string, personID: string , uuid: string  }
    @GetMapping("/user/{ID}")
    public ResponseEntity<User> getUserById(@PathVariable("ID") Long ID) {
        User user = userRepository.selectOneUserByID(ID);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //    informace o všech uživatelích
    // ještě existuje varianta api/v1/users?detail=true
    // Kdy request vrátí rozšířený objekt:
    //
    // Ukázka kódu5
    // {id: string, name: string, surname: string, personID: string , uuid: string
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String name) {
        try {
            List<User> users = new ArrayList<User>();

            if (name == null)
                userRepository.selectAllUsers().forEach(users::add);
            else
                userRepository.findByNameContaining(name).forEach(users::add);

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
    public ResponseEntity<String> updateUser(@PathVariable("ID") Long ID, @RequestBody User user) {
        User userToUpdate = userRepository.selectOneUserByID(ID);

        if (userToUpdate != null) {
            userToUpdate.setName(user.getName());
            userToUpdate.setSurname(user.getSurname());

            userRepository.updateUser(userToUpdate);
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
