package com.angoor.project.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CalendarToken")
public class CalendarToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Link to the Person owning this wallet
    @OneToOne(mappedBy = "calendartoken")
    private Person person;
    
    @Column(nullable = false)
    private String accessToken;
    
    @Column(nullable = false)
    private String refreshToken;
    
    @Column(nullable = false)
    private LocalDateTime expireAfter;

   
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public LocalDateTime getExpireAfter() {
		return expireAfter;
	}

	public void setExpireAfter(LocalDateTime expireAfter) {
		this.expireAfter = expireAfter;
	}
    
    
    
}
