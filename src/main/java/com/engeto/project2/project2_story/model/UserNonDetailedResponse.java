package com.engeto.project2.project2_story.model;

import com.fasterxml.jackson.annotation.JsonView;

public class UserNonDetailedResponse {

    // Atributy
    private Long ID;
    private String name;
    private String surname;
    // endregion


    // region Konstruktory
    public UserNonDetailedResponse(Long ID, String name, String surname) {
        this.ID = ID;
        this.name = name;
        this.surname = surname;
    }
    public UserNonDetailedResponse() {
    }
    // endregion


    // region Gettery a Settery
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
    // endregion
}
