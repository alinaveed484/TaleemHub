package com.angoor.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_teacher_request")
public class StudentTeacherRequest {

    @Id
    @Column(name = "student_id") // Match exactly with the database column name
    private Integer studentId;

    @Column(name = "teacher_id") // Match exactly with the database column name
    private Integer teacherId;

    // Getters and setters
    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
}
