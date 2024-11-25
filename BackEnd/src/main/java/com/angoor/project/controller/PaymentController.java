package com.angoor.project.controller;

import com.angoor.project.model.Payment;
import com.angoor.project.model.Person;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PersonRepo personRepo;

    @Autowired
    public PaymentController(PaymentService paymentService, PersonRepo personRepo) {
        this.paymentService = paymentService;
        this.personRepo = personRepo;
    }

    // Endpoint to check and validate IBAN
    @PostMapping("/{personId}/check-iban")
    public ResponseEntity<Boolean> checkIBAN(@PathVariable Integer personId, @RequestParam String iban) {
        boolean isValid = paymentService.checkIBAN(personId, iban);
        return ResponseEntity.ok(isValid);
    }

    // Endpoint to transfer the amount from wallet to bank
    @PostMapping("/{personId}/transfer-amount")
    public ResponseEntity<Payment> transferAmount(@PathVariable Integer personId) {
        Payment payment = paymentService.transferAmount(personId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/checkWallet")
    public ResponseEntity<Map<String,Double>> checkWallet(@RequestParam String uid) {
        Person person = personRepo.findByUid(uid).orElseThrow(() -> new RuntimeException("Person not found"));
        double currency = person.getWallet().getCurrency();

        Map<String, Double> response = new HashMap<>();
        response.put("currency", currency);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/convertPoints")
    public ResponseEntity<Map<String,String>> convertPoints(@RequestParam String uid) {
        Person person = personRepo.findByUid(uid).orElseThrow(() -> new RuntimeException("Person not found"));

        Integer points = person.getPoints();
        person.getWallet().setCurrency(points + person.getWallet().getCurrency());

        person.setPoints(0);

        personRepo.save(person);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cashOut")
    public ResponseEntity<Map<String,String>> cashOut(@RequestParam String uid) {
        Person person = personRepo.findByUid(uid).orElseThrow(() -> new RuntimeException("Person not found"));

        person.getWallet().clearWallet();

        personRepo.save(person);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/storeCard")
    public ResponseEntity<Map<String,String>> storeCard(@RequestParam String uid, @RequestParam String cardNumber) {
        Person person = personRepo.findByUid(uid).orElseThrow(() -> new RuntimeException("Person not found"));

        person.getWallet().setCardNumber(cardNumber);

        personRepo.save(person);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Success");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getCard")
    public ResponseEntity<Map<String,String>> getCard(@RequestParam String uid) {
        Person person = personRepo.findByUid(uid).orElseThrow(() -> new RuntimeException("Person not found"));
        String cardNumber = person.getWallet().getCardNumber();
        String lastFourDigits = cardNumber != null && cardNumber.length() >= 4
                ? cardNumber.substring(cardNumber.length() - 4)
                : "XXXX";

        // Prepare response
        Map<String, String> response = new HashMap<>();
        response.put("card", lastFourDigits);
        return ResponseEntity.ok(response);
    }
}
