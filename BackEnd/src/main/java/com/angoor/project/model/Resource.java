package com.angoor.project.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



@Entity
@Table(name = "resource")
public class Resource {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Integer resourceID;
	
	@Column(name = "title", nullable = false)
	private String title;
	
    @ManyToOne
    @JoinColumn(name = "uploader_id", nullable = false)
    private Person uploader;  // Each Resource is linked to a Person (Student/Teacher)
    
    @Enumerated(EnumType.STRING)
	@Column(name = "resource_category", nullable = false)
	private resource_category resourceCategory;
	
	@Column(name = "resource_url", nullable = false)
	private String resourceUrl;
	
    public Resource(Integer resourceID,String title, Person uploader, resource_category resourceCategory,String resourceUrl) {
        this.resourceID = resourceID;
        this.title = title;
        this.uploader = uploader;
        this.resourceCategory = resourceCategory;
        this.resourceUrl = resourceUrl;
    }
    public Resource(String title, Person uploader, resource_category resourceCategory,String resourceUrl) {
        this.title = title;
        this.uploader = uploader;
        this.resourceCategory = resourceCategory;
        this.resourceUrl = resourceUrl;
    }
    public Resource() {
    	
    }
	public String getTitle() {
		return title;
	}
	public String getResourceCategory() {
		return resourceCategory.toString();
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setResourceCategory(resource_category resourceCategory) {
		this.resourceCategory = resourceCategory;
	}
	public Person getUploader() {
		return uploader;
	}
	public void setUploader(Person uploader) {
		this.uploader = uploader;
	}

	public Integer getResourceID() {
		return resourceID;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceID(Integer resourceID) {
		this.resourceID = resourceID;
	}
	
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
    
}

