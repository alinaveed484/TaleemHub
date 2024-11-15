package com.angoor.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angoor.project.model.CalendarToken;


@Repository
public interface CalendarTokenRepo extends JpaRepository<CalendarToken, Integer> {

}