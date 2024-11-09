package com.angoor.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angoor.project.model.Chat;
import com.angoor.project.model.Comment;

@Repository
public interface ChatRepo extends JpaRepository<Chat,Integer> {

}
