package com.medilabo.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.medilabo.user_service.models.DTO.requests.UserRequest;
import com.medilabo.user_service.models.DTO.responses.UserResponse;
import com.medilabo.user_service.models.entities.User;
import com.medilabo.user_service.repositories.interfaces.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserResponse getUserByUsername(String username) throws Exception {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new Exception("User not found");
        }
        return UserResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .password(user.getPassword())
                .build();
    }

    public UserResponse save(UserRequest userRequest) throws Exception {
        try {
            if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
                throw new Exception("Username already exists");
            }
        } catch (Exception e) {
            throw new Exception("Error checking username: " + e.getMessage());
        }
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role("USER")
                .build();
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("Error saving user: " + e.getMessage());
        }
        return UserResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .password(user.getPassword())
                .build();
    }

}
