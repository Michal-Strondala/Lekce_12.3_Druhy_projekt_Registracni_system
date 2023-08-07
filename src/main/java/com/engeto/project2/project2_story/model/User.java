package com.engeto.project2.project2_story.model;


import com.fasterxml.jackson.annotation.JsonView;

public class User {

    private Long ID;
    private String name;
    private String surname;
    private String personID;
    // UUID - Generování
    // Pro každý nový záznam musí být vygenerovaný ještě UUID, což bude další jedinečný identifikátor uživatele.
    private byte[] uuid;



    public User(Long id, String name, String surname, String personID, byte[] uuid) {
        this.ID = id;
        this.name = name;
        this.surname = surname;
        this.personID = personID;
        this.uuid = uuid;  // Generování náhodného UUID
    }

    public User(String name, String surname, String personID) {
        this.name = name;
        this.surname = surname;
        this.personID = personID;
    }
    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
    public User() {
    }

    @JsonView(Views.Public.class)
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
    @JsonView(Views.Public.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @JsonView(Views.Public.class)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonView(Views.Public.class)
    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        if (personID.length() == 12)
            this.personID = personID;
    }

    @JsonView(Views.Public.class)
    public byte[] getUuid() {
        return uuid;
    }

    public void setUuid(byte[] uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", personID='" + personID + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
