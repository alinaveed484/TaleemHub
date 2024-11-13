package com.angoor.project.controller;

import com.angoor.project.model.Person;
import com.angoor.project.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    PersonController(PersonService personService) {
        this.personService = personService;
    }

    // Endpoint to redeem points
    @PostMapping("/{personId}/redeem")
    public ResponseEntity<Person> redeemPoints(@PathVariable Integer personId) {
        Person updatedPerson = personService.redeemPoints(personId);
        return ResponseEntity.ok(updatedPerson);
    }
}