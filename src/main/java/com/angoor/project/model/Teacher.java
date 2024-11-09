package com.angoor.project.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

enum Qualification {
    Bachelors, Masters, PhD
}

@Entity
@Table(name = "Teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacherId")
    private Integer teacherId;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "yearsExperience")
    private Integer yearsExperience;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "hireDate")
    private LocalDate hireDate;
    @Column(name = "subjectSpecialization")
    private String subjectSpecialization;
    //This way only from the 3 Options data Will be stored in the Database
    @Enumerated(EnumType.STRING)
    @Column(name = "qualification")
    private Qualification qualification;
    @Column(name = "profilePhotoURL")
    private String profilePhotoURL;




    @ManyToMany(mappedBy = "teachers")
    private Set<Student> students = new HashSet<>();

    public Teacher(String firstName, String lastName, int yearsExperience, String email, String phone, LocalDate hireDate, String subjectSpecialization, Qualification qualification, String profilePhotoURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearsExperience = yearsExperience;
        this.email = email;
        this.phone = phone;
        this.hireDate = hireDate;
        this.subjectSpecialization = subjectSpecialization;
        this.qualification = qualification;
        this.profilePhotoURL = profilePhotoURL;
    }

    public Teacher() {

    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setYearsExperience(int yearsExperience) {
        this.yearsExperience = yearsExperience;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    public void setSubjectSpecialization(String subjectSpecialization) {
        this.subjectSpecialization = subjectSpecialization;
    }
    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }
    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }
    public int getTeacherId() {
        return teacherId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getYearsExperience() {
        return yearsExperience;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public LocalDate getHireDate() {
        return hireDate;
    }
    public String getSubjectSpecialization() {
        return subjectSpecialization;
    }
    public Qualification getQualification() {
        return qualification;
    }
    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }
    public Set<Student> getStudents() {
        return students;
    }
    public void setStudents(Set<Student> students) {
        this.students = students;
    }
    @Override
    public int hashCode() {
        return ID.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Teacher teacher = (Teacher) obj;
        return ID.equals(teacher.ID);
    }
}
