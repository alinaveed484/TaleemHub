package com.angoor.project.repository;


import com.angoor.project.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Integer>{
    List<Comment> findByPost_PostId(Long postId);
}
