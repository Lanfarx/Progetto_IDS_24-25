package it.unicam.cs.filieraagricola.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disabilita CSRF se non necessario per le API REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/register", "/api/login", "/h2-console/**").permitAll() // Permette l'accesso alla console H2 senza autenticazione
                        .anyRequest().authenticated() // Protegge gli altri endpoint
                )
                .httpBasic(Customizer.withDefaults()) // Configura l'autenticazione HTTP Basic
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'self'")) // Abilita la console H2 solo dalla stessa origine
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usa BCrypt per codificare le password
    }
}
