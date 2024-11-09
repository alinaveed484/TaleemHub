package com.angoor.project.repository;


import com.angoor.project.model.Comment;

public class CommentRepo {
	private static CommentRepo instance;

	private CommentRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized CommentRepo getInstance() {
        if (instance == null) {
            instance = new CommentRepo();
        }
        return instance;
    }
}
