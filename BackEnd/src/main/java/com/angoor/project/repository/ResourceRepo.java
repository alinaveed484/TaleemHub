package com.angoor.project.repository;

import com.angoor.project.model.Resource;
import com.angoor.project.model.resource_subject;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResourceRepo extends JpaRepository<Resource, Integer>{
    List<Resource> findBySubject(resource_subject subject);

}
