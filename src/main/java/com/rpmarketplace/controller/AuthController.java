package com.rpmarketplace.controller;

import com.rpmarketplace.model.User;
import com.rpmarketplace.security.JwtService;
import com.rpmarketplace.service.PasswordResetService;
import com.rpmarketplace.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordResetService passwordResetService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService,
                          PasswordResetService passwordResetService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest request) {
        return userService.registerCustomer(request.name(), request.email(), request.password());
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        String token = jwtService.generateToken(user.getEmail(), user.getRoles().stream().map(Enum::name).toList());
        return new TokenResponse(token);
    }

    @PostMapping("/logout")
    public MessageResponse logout() {
        return new MessageResponse("Logout efetuado no cliente.");
    }

    @PostMapping("/password-reset")
    public MessageResponse requestReset(@Valid @RequestBody PasswordResetRequest request) {
        userService.findByEmail(request.email())
                .ifPresent(passwordResetService::requestReset);
        return new MessageResponse("Se o email estiver cadastrado, enviaremos um link de redefinição.");
    }

    @PostMapping("/password-reset/confirm")
    public MessageResponse confirmReset(@Valid @RequestBody PasswordResetConfirm request) {
        boolean success = passwordResetService.resetPassword(request.token(), request.newPassword(), userService);
        if (!success) {
            throw new IllegalArgumentException("Token expirado ou inválido");
        }
        return new MessageResponse("Senha redefinida com sucesso");
    }

    public record RegisterRequest(@NotBlank String name, @Email String email, @NotBlank String password) {
    }

    public record LoginRequest(@Email String email, @NotBlank String password) {
    }

    public record TokenResponse(String token) {
    }

    public record PasswordResetRequest(@Email String email) {
    }

    public record PasswordResetConfirm(@NotBlank String token, @NotBlank String newPassword) {
    }

    public record MessageResponse(String message) {
    }
}
