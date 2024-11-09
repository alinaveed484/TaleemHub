package com.angoor.project.repository;

import com.angoor.project.model.Person;


public class PersonRepo {
	private static PersonRepo instance;

	private PersonRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized PersonRepo getInstance() {
        if (instance == null) {
            instance = new PersonRepo();
        }
        return instance;
    }
}
