package com.angoor.project.service;

import com.angoor.project.model.Person;
import com.angoor.project.model.Resource;
import com.angoor.project.model.resource_category;
import com.angoor.project.model.resource_subject;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.repository.ResourceRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceHub {
	
	private final ResourceRepo resourceRepository;
	private final PersonRepo personRepository;
    private final Path storagePath;
	@Autowired
	public ResourceHub(ResourceRepo resourceRepo, PersonRepo personRe,
					   @Value("${resourcehub.storage-path}") String storagePath) {
		this.resourceRepository = resourceRepo;
		this.personRepository = personRe;
		this.storagePath = Paths.get(storagePath).toAbsolutePath().normalize();
	}
	
    
    public Map<String, Object> getResourceCategories(){
    	Map<String, Object> response = new HashMap<>();
    	response.put("1","Text");
    	response.put("2", "Image");
    	response.put("3", "Document");
    	response.put("4", "Video");
    	return response;
    }
    
    public List<Resource> getResourcesBySubject(resource_subject subject) {
        return resourceRepository.findBySubject(subject);
    }
    
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }
    
    @Transactional 
    public ResponseEntity<String> uploadResource(MultipartFile file, String title, resource_category category, String uploaderId, resource_subject subject, String description){
    	try {
    	    String fileName = file.getOriginalFilename();
			Path filePath = storagePath.resolve(fileName).normalize(); // Use the class-level storagePath

    	    // Ensure the directory exists
			Files.createDirectories(storagePath); // This is a no-op if the directory already exists
    	    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    	    Person person = personRepository.findByUid(uploaderId).orElse(null);
    	    if(person == null) {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person ID not found");
    	    }
    	    
    	    Resource resource = new Resource(title, person, category, filePath.toString(),subject,description);
    	    resource.setUploader(person);
    	    resourceRepository.save(resource);
    	    
    	    return ResponseEntity.ok("File uploaded successfully");

    	} catch (Exception e) {
    	    return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
    	}
        

    }

    // Get resource by ID
    public Resource getResourceById(Integer resourceId) {
        return resourceRepository.findById(resourceId).orElse(null);
    }

    
}
