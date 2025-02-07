package it.unicam.cs.filieraagricola.api.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/richieste/autenticato/**").authenticated()
                        .requestMatchers("/richieste/attesa/**", "/richieste/processa/**","/categorie/**")
                            .hasRole("GESTORE_DELLA_PIATTAFORMA")
                        .requestMatchers("/attivita/**").hasRole("ANIMATORE_DELLA_FILIERA")
                        .requestMatchers("/acquirente/**").hasRole("ACQUIRENTE")
                        .requestMatchers("/produttore/**").hasRole("PRODUTTORE")
                        .requestMatchers("/trasformatore/**").hasRole("TRASFORMATORE")
                        .requestMatchers("/distributore/**").hasRole("DISTRIBUTORE_DI_TIPICITA")
                        .requestMatchers("/curatore/**").hasRole("CURATORE")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Permette di usare password in chiaro
    }
}
