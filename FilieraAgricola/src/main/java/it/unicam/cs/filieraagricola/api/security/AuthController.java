package it.unicam.cs.filieraagricola.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserServiceController userService;
    private final PasswordEncoder passwordEncoder;
    private final WebMvcAutoConfiguration.EnableWebMvcConfiguration enableWebMvcConfiguration;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserServiceController userService, PasswordEncoder passwordEncoder, WebMvcAutoConfiguration.EnableWebMvcConfiguration enableWebMvcConfiguration) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.enableWebMvcConfiguration = enableWebMvcConfiguration;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String password) {
        String encodedPassword = passwordEncoder.encode(password); // Codifica la password
        userService.registerUser(username, encodedPassword, "ROLE_USER"); // Registra l'utente
        return ResponseEntity.ok("User successfully registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {
        logger.info("Attempting login for username: {}", username);

        // Trova l'utente come Optional
        Optional<Users> optionalUser = userService.findByUsername(username);

        if (optionalUser.isEmpty()) {
            logger.warn("Login failed: User with username '{}' not found", username);
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        Users user = optionalUser.get();
        logger.info("User found: {}", username);

        // Log delle password per debugging (solo in ambienti di sviluppo!)
        logger.info("Raw password: {}", password);
        logger.info("Encoded password from database: {}", user.getPassword());
        logger.info("Password matches: {}", passwordEncoder.matches(password, user.getPassword()));

        // Confronta la password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Login failed: Invalid password for username '{}'", username);
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        logger.info("Login successful for user: {}", username);
        return ResponseEntity.ok("Login successful for user: " + username);
    }
}


