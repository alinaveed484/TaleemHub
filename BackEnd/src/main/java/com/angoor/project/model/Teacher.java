package com.angoor.project.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

enum Qualification {
    Bachelors, Masters, PhD
}

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends Person{
    private Integer yearsExperience;
    private LocalDate hireDate;
    private String subjectSpecialization;
    //This way only from the 3 Options data Will be stored in the Database
    @Enumerated(EnumType.STRING)
    private Qualification qualification;


    @ManyToMany(mappedBy = "teachers", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();
    
    @ManyToMany(mappedBy = "teacherRequests", fetch = FetchType.LAZY)
    private Set<Student> studentRequests = new HashSet<>();


    public Teacher() {

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
    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
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
        return (qualification != null) ? qualification.toString() : null;
    }
    public Set<Student> getStudents() {
        return students;
    }
    public void setStudents(Set<Student> students) {
        this.students = students;
    }
    public Set<Student> getStudentRequests(){
        return studentRequests;
    }

    public void addMentorRequest(Student s) {
    	studentRequests.add(s);
    }
    
}
