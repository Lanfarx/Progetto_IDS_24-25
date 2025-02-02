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
                        .requestMatchers("/auth/**").permitAll() // Consenti l'accesso alle API di autenticazione
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/richieste-ruoli/richiesta").authenticated() // Chiunque autenticato può fare richiesta di ruolo
                        .requestMatchers("/richieste-ruoli/attesa", "/richieste-ruoli/processa-richiesta")
                            .hasRole("GESTORE_DELLA_PIATTAFORMA") // Solo i gestori possono processare richieste
                        .requestMatchers("/produttore/**").hasRole("PRODUTTORE")// Consenti l'accesso alla console H2
                        .requestMatchers("/trasformatore/**").hasRole("TRASFORMATORE")// Consenti l'accesso alla console H2
                        .requestMatchers("/distributore/**").hasRole("DISTRIBUTORE_DI_TIPICITA")// Consenti l'accesso alla console H2
                        .requestMatchers("/curatore/**").hasRole("CURATORE")// Consenti l'accesso alla console H2
                        .requestMatchers("/animatore/**").hasRole("ANIMATORE_DELLA_FILIERA")// Consenti l'accesso alla console H2
                        .requestMatchers("/acquirente/**").hasRole("ACQUIRENTE")// Consenti l'accesso alla console H2
                        .requestMatchers("/gestore/**").hasRole("GESTORE_DELLA_PIATTAFORMA")// Consenti l'accesso alla console H2
                        .anyRequest().authenticated() // Proteggi tutti gli altri endpoint
                )
                .csrf(AbstractHttpConfigurer::disable) // Disabilita globalmente il CSRF
                .headers(headers -> headers
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // Consenti frame dalla stessa origine per la console H2
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
