package com.angoor.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angoor.project.model.Payment;
import com.angoor.project.model.Person;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer>{

}