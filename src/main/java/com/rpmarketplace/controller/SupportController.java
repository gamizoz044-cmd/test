package com.rpmarketplace.controller;

import com.rpmarketplace.model.SupportMessage;
import com.rpmarketplace.model.User;
import com.rpmarketplace.service.SupportService;
import com.rpmarketplace.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support")
public class SupportController {
    private final SupportService supportService;
    private final UserService userService;

    public SupportController(SupportService supportService, UserService userService) {
        this.supportService = supportService;
        this.userService = userService;
    }

    @PostMapping("/contact")
    public SupportMessage contact(@Valid @RequestBody SupportRequest request, Principal principal) {
        User user = null;
        if (principal != null) {
            user = userService.findByEmail(principal.getName())
                    .orElse(null);
        }
        return supportService.openTicket(user, request.subject(), request.message());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<SupportMessage> listMessages() {
        return supportService.listAll();
    }

    public record SupportRequest(@NotBlank String subject, @NotBlank String message) {
    }
}
