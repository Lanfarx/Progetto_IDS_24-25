package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/gestisci")
public class GestoreController {

    @Autowired
    private UserService userService;

    @GetMapping("/utente/ruoli")
    public ResponseEntity<Set<UserRole>> getUserRoles(@RequestParam Integer userId) {
        if(userService.existsUser(userId)){
            Set<UserRole> roles = userService.getUserById(userId).get().getRoles();
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/utente/ruoli/rimuovi")
    public ResponseEntity<String> rimuoviRuolo(@RequestParam Integer userId, @RequestParam UserRole role) {
        if(userService.existsUser(userId)){
            Users user = userService.getUserById(userId).get();
            if(user.getRoles().contains(role)){
                userService.rimuoviRuolo(userId, role);
                return new ResponseEntity<>("Ruolo: " + role + " rimosso con successo.", HttpStatus.OK);
            } else return new ResponseEntity<>("L'utente non contiene il ruolo: " + role, HttpStatus.OK);
        } else return new ResponseEntity<>("L'utente non esiste", HttpStatus.OK);
    }

    @PostMapping("/utente/ruoli/aggiungi")
    public ResponseEntity<String> aggiungiRuolo(@RequestParam Integer userId, @RequestParam UserRole role) {
        if(userService.existsUser(userId)){
            Users user = userService.getUserById(userId).get();
            if(!user.getRoles().contains(role)){
                userService.aggiungiRuolo(userId, role);
                return new ResponseEntity<>("Ruolo: " + role + " aggiunto con successo.", HttpStatus.OK);
            } else return new ResponseEntity<>("L'utente già contiene il ruolo: " + role, HttpStatus.OK);
        } else return new ResponseEntity<>("L'utente non esiste", HttpStatus.OK);
    }

    @PostMapping("utente/aggiungi")
    public ResponseEntity<String> aggiungiUtente(@RequestParam String username, @RequestParam String password) {
        if(!userService.existsUserByUsername(username)){
            userService.registerUser(username, password);
            return new ResponseEntity<>("Utente con credenziali: " + username + " e password: " + password + " creato con successo", HttpStatus.OK);
        } else return new ResponseEntity<>("Utente già esistente", HttpStatus.CONFLICT);
    }

    @DeleteMapping("/utente/rimuovi")
    public ResponseEntity<String> rimuoviUtente(@RequestParam Integer userId) {
        if(userService.existsUser(userId)) {
            Users user = userService.getUserById(userId).get();
            userService.delete(user);
            return new ResponseEntity<>("Utente eliminato con successo.", HttpStatus.OK);
        } else return new ResponseEntity<>("Utente non trovato", HttpStatus.NOT_FOUND);
    }
}

