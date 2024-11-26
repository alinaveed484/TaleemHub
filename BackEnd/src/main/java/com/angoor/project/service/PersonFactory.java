package com.angoor.project.service;

import com.angoor.project.model.Person;
import com.angoor.project.model.Qualification;
import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;

import java.time.LocalDate;
import java.util.Map;

public class PersonFactory {

    public static Person createPerson(String role, Map<String, Object> payload) {
        Person person;

        switch (role.toLowerCase()) {
            case "student":
                // Create a Student object
                Integer gradeLevel = (Integer) payload.get("gradeLevel");
                LocalDate enrollmentDate = LocalDate.parse((String) payload.get("enrollmentDate"));

                Student student = new Student();
                student.setGradeLevel(gradeLevel);
                student.setEnrollmentDate(enrollmentDate);
                person = student;
                break;

            case "teacher":
                // Create a Teacher object
                Integer yearsExperience = (Integer) payload.get("yearsExperience");
                LocalDate hireDate = LocalDate.parse((String) payload.get("hireDate"));
                String subjectSpecialization = (String) payload.get("subjectSpecialization");
                Qualification qualification = Qualification.valueOf((String) payload.get("qualification"));

                Teacher teacher = new Teacher();
                teacher.setYearsExperience(yearsExperience);
                teacher.setHireDate(hireDate);
                teacher.setSubjectSpecialization(subjectSpecialization);
                teacher.setQualification(qualification);
                person = teacher;
                break;

            default:
                throw new IllegalArgumentException("Invalid role specified: " + role);
        }

        return person;
    }
}