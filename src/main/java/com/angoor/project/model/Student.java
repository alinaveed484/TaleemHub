package com.angoor.project.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Student")
public class Student{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentId")
    private Integer studentId;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "gradeLevel")
    private Integer gradeLevel;
    @Column(name = "enrollmentDate")
    private LocalDate enrollmentDate;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "profilePhotoURL")
    private String profilePhotoURL;

    @ManyToMany
    @JoinTable(
            name = "Student_Teacher",
            joinColumns = @JoinColumn(name = "studentId"),
            inverseJoinColumns = @JoinColumn(name = "teacherId")
    )
    private Set<Teacher> teachers = new HashSet<>();

    public Student(String firstName, String lastName, int gradeLevel, LocalDate enrollmentDate, String email, String phone, String profilePhotoURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gradeLevel = gradeLevel;
        this.enrollmentDate = enrollmentDate;
        this.email = email;
        this.phone = phone;
        this.profilePhotoURL = profilePhotoURL;
    }

    public Student() {

    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }
    public int getStudentId() {
        return studentId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getGradeLevel() {
        return gradeLevel;
    }
    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }
    public Set<Teacher> getTeachers() {
        return teachers;
    }
    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void setGradeLevel(int gradeLevel) {
        if (gradeLevel < 8 || gradeLevel > 16) {
            throw new IllegalArgumentException("Grade level must be between 8 and 16.");
        }
        this.gradeLevel = gradeLevel;
    }


}
