package com.angoor.project.service;

import com.angoor.project.model.Comment;
import com.angoor.project.model.Post;
import com.angoor.project.repository.StudentRepository;
import com.angoor.project.repository.TeacherRepository;
import com.angoor.project.repository.PostRepo;
import com.angoor.project.repository.CommentRepo;
import com.angoor.project.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class Forum {

    private final PostRepo postRepo;
    private final CommentRepo commentRepo;

    @Autowired
    public Forum(PostRepo postRepo, CommentRepo commentRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }
}
