package com.angoor.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angoor.project.model.Comment;
import com.angoor.project.model.Log;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Integer> {

}
