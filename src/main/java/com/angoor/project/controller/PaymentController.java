package com.angoor.project.controller;

import com.angoor.project.model.Payment;
import com.angoor.project.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
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
}
