package it.unicam.cs.filieraagricola.api.controller.utenti;

import it.unicam.cs.filieraagricola.api.facades.UserFacade;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserFacade userFacade;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return new ResponseEntity<>("Username e password sono obbligatori", HttpStatus.BAD_REQUEST);
        }

        Optional<Users> existingUser = userFacade.findByUsername(username);
        if (existingUser.isPresent()) {
            return login(username, password);
        }

        userFacade.registerUser(username, password);
        return new ResponseEntity<>("Utente: " + username + " registrato correttamente e autenticato.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {
        Optional<Users> user = userFacade.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("Username non valido", HttpStatus.UNAUTHORIZED);
        }

        if (!userFacade.verifyPassword(password, user.get().getPassword())) {
            return new ResponseEntity<>("Password non valida", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("Login effettuato per l'utente: " + username + " con i ruoli: " + user.get().getRoles(), HttpStatus.OK);
    }
}


