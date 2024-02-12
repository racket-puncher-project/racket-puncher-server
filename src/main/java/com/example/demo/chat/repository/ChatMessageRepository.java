package com.example.demo.chat.repository;

import com.example.demo.entity.ChatMessage;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ChatMessageRepository extends CrudRepository<ChatMessage, ChatMessageId> {
    List<ChatMessage> findAllByMatchingId(String matchingId);
    int countByMatchingId(String matchingId);
}