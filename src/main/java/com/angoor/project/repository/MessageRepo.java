package com.angoor.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angoor.project.model.Message;
import com.angoor.project.model.Payment;

@Repository
public interface MessageRepo extends JpaRepository<Message,Integer>{

}
