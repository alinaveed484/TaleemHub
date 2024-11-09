package com.angoor.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angoor.project.model.Log;
import com.angoor.project.model.Message;

@Repository
public interface LogRepo extends JpaRepository<Log,Integer> {

}
