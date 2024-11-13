package com.angoor.project.repository;

import com.angoor.project.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResourceRepo extends JpaRepository<Resource, Integer>{

}
