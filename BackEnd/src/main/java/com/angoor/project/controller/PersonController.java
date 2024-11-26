package com.angoor.project.controller;

import com.angoor.project.model.Person;
import com.angoor.project.model.Qualification;
import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.service.PersonFactory;
import com.angoor.project.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/{uid}/points")
    public ResponseEntity<Map<String, Integer>> getUserPoints(@PathVariable String uid) {
        Optional<Person> person = personRepo.findByUid(uid);

        if (person.isPresent()) {
            Map<String, Integer> response = new HashMap<>();
            response.put("points", person.get().getPoints());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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


            // Common fields
            String firstName = (String) payload.get("firstname");
            String lastName = (String) payload.get("lastname");
            String email = (String) payload.get("email");
            String phone = (String) payload.get("phone");
            String uid = (String) payload.get("uid");

            Person person = PersonFactory.createPerson(role, payload);

            // Set common fields
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setEmail(email);
            person.setPhone(phone);
            person.setUid(uid);

            if(personRepo.findByUid(uid).isEmpty()) {
                // Save person (replace with your repository/service logic)
                personRepo.save(person);
            }

            return ResponseEntity.ok(person);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while saving the user: " + e.getMessage());
        }
    }

}