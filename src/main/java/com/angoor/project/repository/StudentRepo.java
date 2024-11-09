package com.angoor.project.repository;

import com.angoor.project.model.Student;


public class StudentRepo {
	
	private static StudentRepo instance;

	private StudentRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized StudentRepo getInstance() {
        if (instance == null) {
            instance = new StudentRepo();
        }
        return instance;
    }
}
