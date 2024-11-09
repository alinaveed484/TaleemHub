package com.angoor.project.repository;

import com.angoor.project.model.Calender;


public class CalenderRepo {
	private static CalenderRepo instance;

	private CalenderRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized CalenderRepo getInstance() {
        if (instance == null) {
            instance = new CalenderRepo();
        }
        return instance;
    }
}
