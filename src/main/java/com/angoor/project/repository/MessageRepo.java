package com.angoor.project.repository;

import com.angoor.project.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Integer> {
    List<Message> findByChatChatId(Integer chatId);
    // Add any custom query methods if needed
}
