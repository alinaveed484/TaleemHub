package com.angoor.project.repository;

import com.angoor.project.model.Wallet;


public class WalletRepo {
	private static WalletRepo instance;
	
	
	private WalletRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized WalletRepo getInstance() {
        if (instance == null) {
            instance = new WalletRepo();
        }
        return instance;
    }
}
