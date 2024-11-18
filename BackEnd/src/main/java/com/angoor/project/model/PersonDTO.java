package com.angoor.project.model;

public class PersonDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private boolean status;
    private String type;

    public boolean isStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PersonDTO(Integer id, String firstName, String lastName, boolean status, String type) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.type = type;
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



    // getters and setters
}

