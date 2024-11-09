package com.angoor.project.repository;

import com.angoor.project.model.Post;


public class PostRepo {
	private static PostRepo instance;

	private PostRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized PostRepo getInstance() {
        if (instance == null) {
            instance = new PostRepo();
        }
        return instance;
    }
}
