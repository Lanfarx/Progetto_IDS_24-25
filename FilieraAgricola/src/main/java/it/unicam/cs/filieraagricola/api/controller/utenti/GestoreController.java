package it.unicam.cs.filieraagricola.api.controller.utenti;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.facades.UserFacade;
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
    private UserFacade userFacade;

    @GetMapping("/utente/ruoli")
    public ResponseEntity<Set<UserRole>> getUserRoles(@RequestParam Integer userId) {
        if(userFacade.existsUser(userId)){
            Set<UserRole> roles = userFacade.getUserById(userId).get().getRoles();
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/utente/ruoli/rimuovi")
    public ResponseEntity<String> rimuoviRuolo(@RequestParam Integer userId, @RequestParam UserRole role) {
        if(userFacade.existsUser(userId)){
            Users user = userFacade.getUserById(userId).get();
            if(user.getRoles().contains(role)){
                userFacade.rimuoviRuolo(userId, role);
                return new ResponseEntity<>("Ruolo: " + role + " rimosso con successo.", HttpStatus.OK);
            } else return new ResponseEntity<>("L'utente non contiene il ruolo: " + role, HttpStatus.OK);
        } else return new ResponseEntity<>("L'utente non esiste", HttpStatus.OK);
    }

    @PostMapping("/utente/ruoli/aggiungi")
    public ResponseEntity<String> aggiungiRuolo(@RequestParam Integer userId, @RequestParam UserRole role) {
        if(userFacade.existsUser(userId)){
            Users user = userFacade.getUserById(userId).get();
            if(!user.getRoles().contains(role)){
                userFacade.aggiungiRuolo(userId, role);
                return new ResponseEntity<>("Ruolo: " + role + " aggiunto con successo.", HttpStatus.OK);
            } else return new ResponseEntity<>("L'utente già contiene il ruolo: " + role, HttpStatus.OK);
        } else return new ResponseEntity<>("L'utente non esiste", HttpStatus.OK);
    }

    @PostMapping("utente/aggiungi")
    public ResponseEntity<String> aggiungiUtente(@RequestParam String username, @RequestParam String password) {
        if(!userFacade.existsUserByUsername(username)){
            userFacade.registerUser(username, password);
            return new ResponseEntity<>("Utente con credenziali: " + username + " e password: " + password + " creato con successo", HttpStatus.OK);
        } else return new ResponseEntity<>("Utente già esistente", HttpStatus.CONFLICT);
    }

    @DeleteMapping("/utente/rimuovi")
    public ResponseEntity<String> rimuoviUtente(@RequestParam Integer userId) {
        if(userFacade.existsUser(userId)) {
            Users user = userFacade.getUserById(userId).get();
            userFacade.deleteUser(user);
            return new ResponseEntity<>("Utente eliminato con successo.", HttpStatus.OK);
        } else return new ResponseEntity<>("Utente non trovato", HttpStatus.NOT_FOUND);
    }
}

