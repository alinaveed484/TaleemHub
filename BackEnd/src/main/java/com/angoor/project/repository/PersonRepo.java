package com.angoor.project.repository;

import com.angoor.project.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer>{
    Optional<Person> findByUid(String uid);
}
