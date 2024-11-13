package com.angoor.project.model;

public class PersonDTO {
    private Integer id;
    private String firstName;
    private String lastName;

    public boolean isStatus() {
        return status;
    }

    public PersonDTO(Integer id, String firstName, String lastName, boolean status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Integer getId() {
        return id;
    }

    private boolean status;

    // getters and setters
}

