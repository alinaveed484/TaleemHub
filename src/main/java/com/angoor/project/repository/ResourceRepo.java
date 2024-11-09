package com.angoor.project.repository;

import com.angoor.project.model.Resource;


public class ResourceRepo {
	private static ResourceRepo instance;

	private ResourceRepo() {
		
	}
	
    // Syncrhonized keyword makes it thread-safe
    public static synchronized ResourceRepo getInstance() {
        if (instance == null) {
            instance = new ResourceRepo();
        }
        return instance;
    }
}
