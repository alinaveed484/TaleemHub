package com.angoor.project.service;

import com.angoor.project.model.Person;
import com.angoor.project.repository.PersonRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PersonService {

    private static final int POINTS_CONVERSION_THRESHOLD = 100;
    private static final int POINTS_TO_CURRENCY_RATE = 10;

    private final PersonRepo personRepository;

    @Autowired
    PersonService(PersonRepo personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public Person redeemPoints(Integer personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new NoSuchElementException("Person not found with ID: " + personId));

        if (person.getPoints() >= POINTS_CONVERSION_THRESHOLD) {
            int currencyToAdd = (person.getPoints() / POINTS_CONVERSION_THRESHOLD) * POINTS_TO_CURRENCY_RATE;
            int remainingPoints = person.getPoints() % POINTS_CONVERSION_THRESHOLD;

            person.getWallet().addCurrency(currencyToAdd);
            person.setPoints(remainingPoints);

            personRepository.save(person);
        }

        return person;
    }
}