package com.angoor.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angoor.project.model.Wallet;

@Repository
public interface WalletRepo  extends JpaRepository<Wallet, Integer>{

}
