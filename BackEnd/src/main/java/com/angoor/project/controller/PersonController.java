package com.angoor.project.controller;

import com.angoor.project.model.Person;
import com.angoor.project.model.Qualification;
import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final PersonRepo personRepo;

    @Autowired
    PersonController(PersonService personService, PersonRepo personRepo) {
        this.personService = personService;
        this.personRepo = personRepo;
    }

    // Endpoint to redeem points
    @PostMapping("/{personId}/redeem")
    public ResponseEntity<Person> redeemPoints(@PathVariable Integer personId) {
        Person updatedPerson = personService.redeemPoints(personId);
        return ResponseEntity.ok(updatedPerson);
    }

    @PostMapping("/save_user")
    public ResponseEntity<?> savePerson(@RequestBody Map<String, Object> payload) {
        try {
            String role = (String) payload.get("role");
            Person person;

            // Common fields
            String firstName = (String) payload.get("firstName");
            String lastName = (String) payload.get("lastName");
            String email = (String) payload.get("email");
            String phone = (String) payload.get("phone");
            String uid = (String) payload.get("uid");

            if ("Student".equalsIgnoreCase(role)) {
                // Student-specific fields
                Integer gradeLevel = (Integer) payload.get("gradeLevel");
                LocalDate enrollmentDate = LocalDate.parse((String) payload.get("enrollmentDate"));

                Student student = new Student();
                student.setGradeLevel(gradeLevel);
                student.setEnrollmentDate(enrollmentDate);
                person = student;
            } else if ("Teacher".equalsIgnoreCase(role)) {
                // Teacher-specific fields
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
            } else {
                return ResponseEntity.badRequest().body("Invalid role specified.");
            }

            // Set common fields
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setEmail(email);
            person.setPhone(phone);
            person.setUid(uid);

            // Save person (replace with your repository/service logic)
            personRepo.save(person);

            return ResponseEntity.ok(person);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while saving the user: " + e.getMessage());
        }
    }

}