package com.angoor.project.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.angoor.project.model.Resource;
import com.angoor.project.model.Student;


@Repository
public interface ResourceRepo extends JpaRepository<Resource, Integer>{

}
