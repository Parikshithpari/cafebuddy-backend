package com.cafebuddy.service;

import com.cafebuddy.dto.AuthResponse;
import com.cafebuddy.dto.LoginRequest;
import com.cafebuddy.dto.RegisterRequest;
import com.cafebuddy.model.User;
import com.cafebuddy.repository.UserRepository;
import com.cafebuddy.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                "This email is already registered. Please sign in instead.");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role("USER")
                .build();

        userRepository.save(user);

        // Send welcome email asynchronously
        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(request.getEmail());
        loginRequest.setPassword(request.getPassword());
        return login(loginRequest);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
