package com.medilabo.user_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.user_service.models.DTO.responses.UserResponse;
import com.medilabo.user_service.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/find-by-username/{username}")
    public ResponseEntity<UserResponse> findByUsername(@PathVariable String username) {
        try {
            UserResponse userResponse = userService.getUserByUsername(username);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
