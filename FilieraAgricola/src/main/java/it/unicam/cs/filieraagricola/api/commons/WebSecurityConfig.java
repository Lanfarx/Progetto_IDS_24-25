package it.unicam.cs.filieraagricola.api.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Consenti l'accesso alle API di autenticazione
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/prodottibase/**").permitAll()// Consenti l'accesso alla console H2
                        .anyRequest().authenticated() // Proteggi tutti gli altri endpoint
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .ignoringRequestMatchers("/prodottibase/**") // Disabilita CSRF solo per la console H2
                        .disable() // Disabilita globalmente il CSRF
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // Consenti frame dalla stessa origine per la console H2
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura sessioni stateless
                )
                .httpBasic(Customizer.withDefaults()); // Abilita autenticazione basic

        return http.build();
    }
}
