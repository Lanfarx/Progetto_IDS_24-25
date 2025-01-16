package it.unicam.cs.filieraagricola.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    public WebSecurityConfig() {
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> {
            requests
                    .requestMatchers("/", "/home").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();
        }).formLogin((form) -> {
            form.loginPage("/login").permitAll();
        }).logout((logout) -> {
            logout.permitAll();
        });
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles(new String[]{"USER"}).build();
        UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("adminpsw").roles(new String[]{"ADMIN"}).build();
        return new InMemoryUserDetailsManager(new UserDetails[]{user, admin});
    }
}
