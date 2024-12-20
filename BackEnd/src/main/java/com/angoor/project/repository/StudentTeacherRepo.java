package com.angoor.project.repository;

import com.angoor.project.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTeacherRepo extends JpaRepository<Person, Integer>{

}
