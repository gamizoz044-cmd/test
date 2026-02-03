package com.rpmarketplace.service;

import com.rpmarketplace.model.PasswordResetToken;
import com.rpmarketplace.model.User;
import com.rpmarketplace.repository.PasswordResetTokenRepository;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.reset.base-url}")
    private String resetBaseUrl;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository, EmailService emailService,
                                PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void requestReset(User user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(OffsetDateTime.now().plusHours(2));
        tokenRepository.save(token);
        String link = resetBaseUrl + "?token=" + token.getToken();
        emailService.sendEmail(user.getEmail(), "Redefinição de senha", "Use o link para redefinir: " + link);
    }

    public boolean resetPassword(String tokenValue, String newPassword, UserService userService) {
        PasswordResetToken token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));
        if (token.isUsed() || token.getExpiresAt().isBefore(OffsetDateTime.now())) {
            return false;
        }
        User user = token.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userService.save(user);
        token.setUsed(true);
        tokenRepository.save(token);
        return true;
    }
}
