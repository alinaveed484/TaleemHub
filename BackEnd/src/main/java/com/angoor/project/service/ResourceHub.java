package com.angoor.project.service;

import jakarta.annotation.PostConstruct;

import com.angoor.project.repository.ResourceRepo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.angoor.project.model.Person;
import com.angoor.project.model.Post;
import com.angoor.project.model.Resource;
import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;
import com.angoor.project.model.resource_category;
import com.angoor.project.repository.CommentRepo;
import com.angoor.project.repository.PersonRepo;

import jakarta.transaction.Transactional;

@Service
public class ResourceHub {
	
	private final ResourceRepo resourceRepository;
	private final PersonRepo personRepository;
    private final String storagePath = "/home/ariyan/Desktop/uploads/";
	@Autowired
	public ResourceHub(ResourceRepo resourceRepo, PersonRepo personRe) {
		this.resourceRepository = resourceRepo;
		this.personRepository = personRe;
	}
	
    
    public Map<String, Object> getResourceCategories(){
    	Map<String, Object> response = new HashMap<>();
    	response.put("1","Text");
    	response.put("2", "Image");
    	response.put("3", "Document");
    	response.put("4", "Video");
    	return response;
    }
    
    @Transactional 
    public ResponseEntity<String> uploadResource(MultipartFile file, String title, resource_category category, Integer uploaderId){
    	try {
    	    String fileName = file.getOriginalFilename();
    	    Path filePath = Paths.get(storagePath, fileName);

    	    // Ensure the directory exists
    	    Files.createDirectories(Paths.get(storagePath)); 
    	    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    	    Person person = personRepository.findById(uploaderId).orElse(null);
    	    if(person == null) {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person ID not found");
    	    }
    	    
    	    Resource resource = new Resource(title, person, category, filePath.toString());
    	    resource.setUploader(person);
    	    resourceRepository.save(resource);
    	    
    	    return ResponseEntity.ok("File uploaded successfully");

    	} catch (Exception e) {
    	    return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
    	}
        

    }

    
}
