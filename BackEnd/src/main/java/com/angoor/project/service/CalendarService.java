package com.angoor.project.service;

import org.springframework.stereotype.Service;

import com.angoor.project.model.CalendarToken;
import com.angoor.project.model.Person;
import com.angoor.project.repository.CalendarTokenRepo;
import com.angoor.project.repository.PersonRepo;

import jakarta.persistence.OneToOne;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CalendarService {
	private final PersonRepo personRepository;
	private final CalendarTokenRepo tokenRepository;
	
	@Autowired
	public CalendarService(PersonRepo personRepo, CalendarTokenRepo tokenRepo) {
		this.personRepository = personRepo;
		this.tokenRepository = tokenRepo;
	}

	public boolean saveTokens(Integer personID, String accessToken, String refreshToken, LocalDateTime expirationTime) {
	    // Retrieve the person from the database using personID
	    Person person = personRepository.findById(personID).orElse(null);
	    
	    // Check if the person exists
	    if (person != null) {
	        // Create a new CalendarToken instance and set its fields
	        CalendarToken calendarToken = new CalendarToken();
	        calendarToken.setAccessToken(accessToken);
	        calendarToken.setRefreshToken(refreshToken);
	        calendarToken.setExpireAfter(expirationTime);
	        
	        // Associate the calendar token with the person
	        calendarToken.setPerson(person);
	        person.setCalendartoken(calendarToken);
	        
	        // Save the CalendarToken instance using the repository
	        tokenRepository.save(calendarToken);
	        
	        // Update the Person entity as well if needed
	        personRepository.save(person);
	        return true;
	    } else {
	        // Handle case where person is not found, maybe throw an exception or log a message
	        System.out.println("Person with ID " + personID + " not found.");
	        return false;
	    }
	}
}
