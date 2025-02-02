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
        userService.registerUser(username, password); // Registra l'utente
        return ResponseEntity.ok("Utente registrato correttamente");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {
        // Recupera l'utente dal database
        Optional<Users> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).body("Username non valido");
        }

        // Verifica la password
        if (!password.equals(user.get().getPassword())) {
            return ResponseEntity.status(401).body("Password non valida");
        }

        return ResponseEntity.ok("Login effettuato per l'utente: " + username + " con i ruoli: " + user.get().getRoles());
    }
}


