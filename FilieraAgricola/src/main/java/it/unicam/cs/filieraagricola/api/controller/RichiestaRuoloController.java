package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.commons.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.services.RichiestaRuoloService;
import it.unicam.cs.filieraagricola.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/richieste-ruoli")
public class RichiestaRuoloController {

    @Autowired
    private RichiestaRuoloService richiestaRuoloService;
    @Autowired
    private UserService userService;

    @PostMapping("/richiesta")
    public ResponseEntity<Object> richiestaRuolo(@RequestParam UserRole ruolo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Users user = userService.findByUsername(username).get();
        if (!user.getRoles().contains(ruolo)) {
            if (!richiestaRuoloService.existsSameRichiesta(user, ruolo)) {
                richiestaRuoloService.aggiungiRichiestaRuolo(user.getId(), ruolo);
                return new ResponseEntity<>("Richiesta di ruolo " + ruolo + " da parte dell'utente: " + user.getId() + " inviata con successo.", HttpStatus.CREATED);
            } else return new ResponseEntity<>("Richiesta da parte di: " + user.getId() + " già effettuata per il ruolo: " + ruolo , HttpStatus.CONFLICT);
        } else return new ResponseEntity<>("L'utente: " + user.getId() + " già ha il ruolo di: " + ruolo, HttpStatus.CONFLICT);
    }

    @GetMapping("/attesa")
    public ResponseEntity<Object> getRichiesteInAttesa() {
        return new ResponseEntity<>(richiestaRuoloService.getRichiesteInAttesa(), HttpStatus.OK);
    }

    @PutMapping("/processa-richiesta")
    public ResponseEntity<Object> processaRichiesta(@RequestParam Integer id, @RequestParam boolean approvato) {
        if(richiestaRuoloService.existsRichiesta(id))
        {
             RichiestaRuolo richiesta = richiestaRuoloService.getRichiesta(id).get();
             StatoRichiesta statoRichiesta = richiesta.getStato();
            if(statoRichiesta == StatoRichiesta.ATTESA){
                richiestaRuoloService.processaRichiesta(id, approvato);
                return new ResponseEntity<>("Richiesta: " + id + (approvato ? " accettata" : " rifiutata"), HttpStatus.OK);
            } else return ResponseEntity.status(409).body("Richiesta già processata con esito: " + statoRichiesta);
        } else {
            return ResponseEntity.status(404).body("Richiesta " + id + " Non Trovata");
        }
    }
}