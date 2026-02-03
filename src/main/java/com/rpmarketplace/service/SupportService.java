package com.rpmarketplace.service;

import com.rpmarketplace.model.SupportMessage;
import com.rpmarketplace.model.User;
import com.rpmarketplace.repository.SupportMessageRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SupportService {
    private final SupportMessageRepository supportMessageRepository;
    private final EmailService emailService;

    public SupportService(SupportMessageRepository supportMessageRepository, EmailService emailService) {
        this.supportMessageRepository = supportMessageRepository;
        this.emailService = emailService;
    }

    public SupportMessage openTicket(User user, String subject, String message) {
        SupportMessage ticket = new SupportMessage();
        ticket.setUser(user);
        ticket.setSubject(subject);
        ticket.setMessage(message);
        SupportMessage saved = supportMessageRepository.save(ticket);
        if (user != null) {
            emailService.sendEmail(user.getEmail(), "Recebemos sua mensagem",
                    "Obrigado pelo contato. Nossa equipe responderá em breve.");
        }
        return saved;
    }

    public List<SupportMessage> listAll() {
        return supportMessageRepository.findAll();
    }
}
