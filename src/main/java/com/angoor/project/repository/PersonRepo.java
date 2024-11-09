package com.angoor.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angoor.project.model.Person;
import com.angoor.project.model.Post;

@Repository
public interface PersonRepo extends JpaRepository<Person,Integer>{

}
