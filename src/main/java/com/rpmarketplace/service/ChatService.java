package com.rpmarketplace.service;

import com.rpmarketplace.model.ChatMessage;
import com.rpmarketplace.model.User;
import com.rpmarketplace.repository.ChatMessageRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveMessage(User sender, String content) {
        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setContent(content);
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> listMessages() {
        return chatMessageRepository.findAll();
    }
}
