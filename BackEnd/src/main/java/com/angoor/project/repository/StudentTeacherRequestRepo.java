package com.angoor.project.repository;

import com.angoor.project.model.StudentTeacherRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTeacherRequestRepo extends JpaRepository<StudentTeacherRequest, Integer> {

    void deleteByStudentIdAndTeacherId(Integer studentId, Integer teacherId);

}
