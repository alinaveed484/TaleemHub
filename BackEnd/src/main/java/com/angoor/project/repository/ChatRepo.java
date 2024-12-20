package com.angoor.project.repository;

import com.angoor.project.model.Chat;
import com.angoor.project.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Integer> {
    Optional<Chat> findByTeacher_idAndStudent_id(Integer teacher_id, Integer student_id);
}
