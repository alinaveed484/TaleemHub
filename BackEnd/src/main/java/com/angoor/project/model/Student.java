package com.angoor.project.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends Person {
    private Integer gradeLevel;
    private LocalDate enrollmentDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Student_Teacher",
            joinColumns = @JoinColumn(name = "studentId"),
            inverseJoinColumns = @JoinColumn(name = "teacherId")
    )
    private Set<Teacher> teachers = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Student_Teacher_Request",
            joinColumns = @JoinColumn(name = "studentId"),
            inverseJoinColumns = @JoinColumn(name = "teacherId")
    )
    private Set<Teacher> teacherRequests = new HashSet<>();

    public Student() {

    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    public Integer getGradeLevel() {
        return gradeLevel;
    }
    public String getPersonType() {
        return "Student";
    }
    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
    public Set<Teacher> getTeachers() {
        return teachers;
    }
    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }
    public Set<Teacher> getTeacherRequests() {
        return teacherRequests;
    }
    public void addMentorRequest(Teacher t) {
    	teacherRequests.add(t);
    }

    public void setGradeLevel(int gradeLevel) {
        if (gradeLevel < 8 || gradeLevel > 16) {
            throw new IllegalArgumentException("Grade level must be between 8 and 16.");
        }
        this.gradeLevel = gradeLevel;
    }
}
