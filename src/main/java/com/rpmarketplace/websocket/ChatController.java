package com.rpmarketplace.websocket;

import com.rpmarketplace.model.ChatMessage;
import com.rpmarketplace.model.User;
import com.rpmarketplace.service.ChatService;
import com.rpmarketplace.service.UserService;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void send(@Payload ChatMessageRequest request) {
        User sender = userService.findByEmail(request.senderEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        ChatMessage message = chatService.saveMessage(sender, request.content());
        List<String> roles = sender.getRoles().stream().map(Enum::name).toList();
        messagingTemplate.convertAndSend("/topic/chat", new ChatMessageResponse(
                message.getId(), sender.getName(), roles, message.getContent(), message.getSentAt()));
    }

    public record ChatMessageRequest(String senderEmail, String content) {
    }

    public record ChatMessageResponse(Long id, String senderName, List<String> roles, String content,
                                      OffsetDateTime sentAt) {
    }
}
