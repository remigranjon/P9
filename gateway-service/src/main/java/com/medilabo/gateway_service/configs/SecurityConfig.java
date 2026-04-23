package com.medilabo.gateway_service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // 1. Désactivation du CSRF pour simplifier les appels API (à adapter en prod)
                .csrf(csrf -> csrf.disable())

                .authorizeExchange(exchanges -> exchanges
                        // Permettre l'accès aux ressources statiques du Front
                        .pathMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // Permettre l'accès à la page de login et au service d'auth
                        .pathMatchers("/auth/**", "/login").permitAll()

                        // Permettre aux healthchecks Docker de passer sans auth
                        .pathMatchers("/actuator/health/**").permitAll()

                        // TOUT le reste (APIs Patients, Pages Front) nécessite une authentification
                        .anyExchange().authenticated())

                // 2. Configuration du formulaire de login (géré par la Gateway ou redirigé)
                .formLogin(Customizer.withDefaults())

                // 3. Configuration de la déconnexion
                .logout(logout -> logout.logoutUrl("/auth/logout"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}