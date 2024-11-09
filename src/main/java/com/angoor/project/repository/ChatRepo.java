package com.angoor.project.repository;

import com.angoor.project.model.Chat;


public class ChatRepo {
	private static ChatRepo instance;

	private ChatRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized ChatRepo getInstance() {
        if (instance == null) {
            instance = new ChatRepo();
        }
        return instance;
    }
}
