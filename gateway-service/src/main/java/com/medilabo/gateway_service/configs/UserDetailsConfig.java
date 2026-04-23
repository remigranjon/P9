package com.medilabo.gateway_service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.reactive.function.client.WebClient;

import com.medilabo.gateway_service.models.dto.UserDTO;

@Configuration
public class UserDetailsConfig {

    @Bean
    public ReactiveUserDetailsService userDetailsService(WebClient.Builder builder) {
        // On utilise l'URL injectée par Docker (ex: http://user-service:8081)
        WebClient webClient = builder.baseUrl("http://user-service:${USER_SERVICE_PORT}").build();

        return username -> webClient.get()
                .uri("/users/find-by-username/{username}", username)
                .retrieve()
                // On s'attend à recevoir un DTO (UserDTO) depuis le microservice User
                .bodyToMono(UserDTO.class) 
                .map(userDto -> User.withUsername(userDto.getUsername())
                        .password(userDto.getPassword()) // Le mot de passe doit être déjà encodé en base (BCrypt)
                        .roles(userDto.getRole())
                        .build());
    }
}

