package com.angoor.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angoor.project.model.Post;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer>{

}