package com.cafebuddy.controller;

import com.cafebuddy.dto.PromoEmailRequest;
import com.cafebuddy.dto.UserDto;
import com.cafebuddy.service.EmailService;
import com.cafebuddy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    /** GET /api/users/me — currently logged-in user's profile */
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserByEmail(authentication.getName()));
    }

    /** GET /api/users — ADMIN only — all users list */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> listAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /** GET /api/users/{id} — ADMIN only */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * POST /api/users/send-promo — ADMIN only
     * Sends a promotional email to all registered users.
     * Body: { "subject": "...", "body": "..." }
     */
    @PostMapping("/send-promo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sendPromo(
            @Valid @RequestBody PromoEmailRequest request) {
        int sent = emailService.sendPromoToAllUsers(request.getSubject(), request.getBody());
        return ResponseEntity.ok(Map.of(
                "message", "Promotional email sent successfully",
                "emailsSent", sent
        ));
    }
}
