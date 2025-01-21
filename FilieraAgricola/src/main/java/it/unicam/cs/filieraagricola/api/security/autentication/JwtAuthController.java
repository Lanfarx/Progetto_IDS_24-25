package it.unicam.cs.filieraagricola.api.security.autentication;

import it.unicam.cs.filieraagricola.api.security.UserServiceController;
import it.unicam.cs.filieraagricola.api.security.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class JwtAuthController {

    private final UserServiceController userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthController(UserServiceController userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String password) {
        userService.registerUser(username, password, "ROLE_USER");
        return ResponseEntity.ok("User successfully registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {
        Optional<Users> user = userService.findByUsername(username);

        if (user.isPresent()) {
            Users realUser = user.get();

            if (passwordEncoder.matches(password, realUser.getPassword())) {
                // Genera il token JWT
                String token = jwtUtil.generateToken(realUser.getUsername());
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(401).body("Invalid password");
            }
        }

        return ResponseEntity.status(404).body("User not found");
    }
}
