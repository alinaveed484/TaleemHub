package com.angoor.project.service;

import com.angoor.project.model.Payment;
import com.angoor.project.model.Person;
import com.angoor.project.model.Wallet;
import com.angoor.project.repository.PaymentRepo;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepo paymentRepository;
    private final PersonRepo personRepository;
    private final WalletRepo walletRepository;

    @Autowired
    public PaymentService(PaymentRepo paymentRepository, PersonRepo personRepository, WalletRepo walletRepository) {
        this.paymentRepository = paymentRepository;
        this.personRepository = personRepository;
        this.walletRepository = walletRepository;
    }

    // Method to check IBAN and validate it for a person
    public boolean checkIBAN(Integer personId, String iban) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));

        Payment payment = new Payment(0.0); // Placeholder payment to initialize
        payment.setPerson(person);
        return payment.checkIBAN(iban);
    }

    // Method to transfer the amount from a person's wallet to their bank
    @Transactional
    public Payment transferAmount(Integer personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));

        Wallet wallet = person.getWallet();
        if (wallet == null || wallet.getCurrency() <= 0) {
            throw new IllegalArgumentException("Insufficient funds in wallet.");
        }

        double walletBalance = wallet.getCurrency();
        Payment payment = new Payment(walletBalance); // Set the payment amount
        payment.setPerson(person);
        payment.transferAmount();

        // Clear wallet currency after transferring funds
        wallet.setCurrency(0);
        walletRepository.save(wallet); // Update wallet in the database

        return paymentRepository.save(payment); // Save payment record after transferring
    }

}