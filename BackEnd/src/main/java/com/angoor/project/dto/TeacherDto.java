package com.angoor.project.dto;

import java.time.LocalDate;

public class TeacherDto {
    private String firstName;
    private String lastName;
    private String profilePhotoURL;
    private Integer yearsExperience;
    private LocalDate hireDate;
    private String subjectSpecialization;
    private String qualification;

    // Constructors
    public TeacherDto() {
    }

    public TeacherDto(String firstName, String lastName, String profilePhotoURL,
                      Integer yearsExperience, LocalDate hireDate,
                      String subjectSpecialization, String qualification) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePhotoURL = profilePhotoURL;
        this.yearsExperience = yearsExperience;
        this.hireDate = hireDate;
        this.subjectSpecialization = subjectSpecialization;
        this.qualification = qualification;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }
    public void setYearsExperience(Integer yearsExperience) {
        this.yearsExperience = yearsExperience;
    }
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    public void setSubjectSpecialization(String subjectSpecialization) {
        this.subjectSpecialization = subjectSpecialization;
    }
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }
    public Integer getYearsExperience() {
        return yearsExperience;
    }
    public LocalDate getHireDate() {
        return hireDate;
    }
    public String getSubjectSpecialization() {
        return subjectSpecialization;
    }
    public String getQualification() {
        return qualification;
    }
}