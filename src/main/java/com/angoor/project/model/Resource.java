package com.angoor.project.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

enum uploader_type {
    Teacher,Student
}

@Entity
@Table(name = "resource")
public class Resource {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Integer resourceID;
	@Column(name = "uploader_id")
	private Integer uploaderID;
	@Enumerated(EnumType.STRING)
	@Column(name = "uploader_type")
	private uploader_type uploaderType;
	@Column(name = "resource_type")
	private String resourceType;
	@Column(name = "resource_url")
	private String resourceUrl;
	
    public Resource(Integer resourceID, Integer uploaderID, uploader_type uploaderType, String resourceType,String resourceUrl) {
        this.resourceID = resourceID;
        this.uploaderID = uploaderID;
        this.uploaderType = uploaderType;
        this.resourceType = resourceType;
        this.resourceUrl = resourceUrl;
    }
    public Resource() {
    	
    }
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public Integer getResourceID() {
		return resourceID;
	}
	public Integer getUploaderID() {
		return uploaderID;
	}
	public String getUploaderType() {
		return uploaderType.toString();
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceID(Integer resourceID) {
		this.resourceID = resourceID;
	}
	public void setUploaderID(Integer uploaderID) {
		this.uploaderID = uploaderID;
	}
	public void setUploaderType(String uploaderType) {
		this.uploaderType = uploader_type.valueOf(uploaderType);
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
    
}
