package com.angoor.project.repository;

import com.angoor.project.model.Payment;


public class PaymentRepo {
	private static PaymentRepo instance;

	private PaymentRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized PaymentRepo getInstance() {
        if (instance == null) {
            instance = new PaymentRepo();
        }
        return instance;
    }
}
