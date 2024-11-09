package com.angoor.project.service;

import jakarta.annotation.PostConstruct;

import com.angoor.project.repository.ResourceRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.angoor.project.model.Resource;
import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;
import com.angoor.project.repository.CommentRepo;
import jakarta.transaction.Transactional;

@Service
public class ResourceHub {
	
	private final ResourceRepo resourceRepository;


    private static List<Resource> resourcesInMemory = new ArrayList<>();
    private List<String> resourceCategories = new ArrayList<>();
    
	@Autowired
	public ResourceHub(ResourceRepo resourceRepo) {
		this.resourceRepository = resourceRepo;
	}
	
    @PostConstruct
    public void init() {
    	resourcesInMemory = resourceRepository.findAll();

    }
    
    public Map<String, Object> getResourceCategories(){
    	Map<String, Object> response = new HashMap<>();
    	Integer num=1;
    	
    	for(String s:resourceCategories) {
    		response.put(num.toString(),s);
    	}
    	return response;
    }
    
    @Transactional 
    public void uploadResource(String category, String url,Integer uploaderID,String uploaderType){
    	if(!resourceCategories.contains(category)) {
    		resourceCategories.add(category);
    	}
    	Resource resource = new Resource();
    	resource.setResourceType(category);
    	resource.setResourceUrl(url);
    	resource.setUploaderID(uploaderID);
    	resource.setUploaderType(uploaderType);
    	
    	
    	resourcesInMemory.add(resource);
        resourceRepository.save(resource);
    }
    
}
