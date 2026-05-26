package com.cafebuddy.service;

import com.cafebuddy.dto.UserDto;
import com.cafebuddy.model.User;
import com.cafebuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    private UserDto toDto(User u) {
        return UserDto.builder()
                .id(u.getId())
                .fullName(u.getFullName())
                .email(u.getEmail())
                .phoneNumber(u.getPhoneNumber())
                .role(u.getRole())
                .enabled(u.getEnabled())
                .createdAt(u.getCreatedAt())
                .build();
    }
}
