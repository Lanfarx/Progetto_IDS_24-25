package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.entities.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String password) {
        userService.registerUser(username, password, "ROLE_USER"); // Registra l'utente
        return ResponseEntity.ok("User successfully registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {
        // Recupera l'utente dal database
        Optional<Users> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid username");
        }

        // Verifica la password
        if (!password.equals(user.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid password");
        }

        return ResponseEntity.ok("Login successful for user: " + username);
    }
}


