package com.angoor.project.model;

import jakarta.persistence.*;
import org.apache.commons.validator.routines.IBANValidator;

import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public Payment(Double amount) {
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
    }

    public Payment(Double amount, Person person) {
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
        this.person = person;
    }


    // Empty constructor for JPA
    protected Payment() {}

    public boolean checkIBAN(String iban) {
        // Check if the IBAN is valid according to IBAN format rules
        IBANValidator ibanValidator = new IBANValidator();
        if (ibanValidator.isValid(iban)) {
            // Set IBAN if it's valid and not already set
            if (person.getIban() == null) {
                person.setIban(iban);
                return true;  // Assume IBAN is valid
            }
            return true;
        } else {
            // IBAN is invalid
            return false;
        }
    }

    // Function to transfer the amount from wallet to bank
    public void transferAmount() {
        Wallet wallet = person.getWallet();
        if (wallet.getCurrency() > 0) {
            this.amount = wallet.getCurrency(); // Set the payment amount
            wallet.clearWallet(); // Empty the person's wallet
            // Transfer logic here (currently left empty)
        } else {
            throw new IllegalArgumentException("Insufficient funds in wallet.");
        }
    }
    public void setPerson(Person person) {
        this.person = person;
    }
    public Person getPerson() {
        return person;
    }
}
