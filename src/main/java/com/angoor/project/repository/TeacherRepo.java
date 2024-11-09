package com.angoor.project.repository;

import java.util.List;

import com.angoor.project.model.Teacher;


public class TeacherRepo {
	private static TeacherRepo instance;
	
	
	private TeacherRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized TeacherRepo getInstance() {
        if (instance == null) {
            instance = new TeacherRepo();
        }
        return instance;
    }
    
    public List<Teacher> getAllTeachers(){
    	
    	
    	
    	return null;
    }
    
}
