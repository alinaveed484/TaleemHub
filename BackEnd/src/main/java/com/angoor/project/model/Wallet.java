package com.angoor.project.model;


import jakarta.persistence.*;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double currency = 0; // default currency

    // Link to the Person owning this wallet
    @OneToOne(mappedBy = "wallet")
    private Person person;

    public Integer getId() {
        return id;
    }
    public double getCurrency() {
        return currency;
    }
    public Person getPerson() {
        return person;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setCurrency(double currency) {
        this.currency = currency;
    }
    public void setPerson(Person person) {
        this.person = person;
    }
    public void addCurrency(int amount) {
        this.currency += amount;
    }
    // Constructors
    public Wallet() {
    }
    public void clearWallet() {
        this.currency = 0.0;
    }


}
