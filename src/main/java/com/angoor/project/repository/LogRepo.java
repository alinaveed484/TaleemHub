package com.angoor.project.repository;

import com.angoor.project.model.Log;


public class LogRepo {
	private static LogRepo instance;

	private LogRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized LogRepo getInstance() {
        if (instance == null) {
            instance = new LogRepo();
        }
        return instance;
    }
}
