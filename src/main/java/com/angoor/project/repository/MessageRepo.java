package com.angoor.project.repository;

import com.angoor.project.model.Message;


public class MessageRepo {
	private static MessageRepo instance;

	private MessageRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized MessageRepo getInstance() {
        if (instance == null) {
            instance = new MessageRepo();
        }
        return instance;
    }
}
