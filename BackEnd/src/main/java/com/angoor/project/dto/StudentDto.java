package com.angoor.project.dto;

import java.time.LocalDate;

public class StudentDto {
	private Integer id;
    private String firstName;
    private String lastName;
    private String profilePhotoURL;
    private Integer gradeLevel;
    private LocalDate enrollmentDate;
	public StudentDto(Integer id, String firstName, String lastName, String profilePhotoURL, Integer gradeLevel, LocalDate enrollmentDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.profilePhotoURL = profilePhotoURL;
		this.gradeLevel = gradeLevel;
		this.enrollmentDate = enrollmentDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getProfilePhotoURL() {
		return profilePhotoURL;
	}
	public void setProfilePhotoURL(String profilePhotoURL) {
		this.profilePhotoURL = profilePhotoURL;
	}
	public Integer getGradeLevel() {
		return gradeLevel;
	}
	public void setGradeLevel(Integer gradeLevel) {
		this.gradeLevel = gradeLevel;
	}
	public LocalDate getEnrollmentDate() {
		return enrollmentDate;
	}
	public void setEnrollmentDate(LocalDate enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}
    
    
}
