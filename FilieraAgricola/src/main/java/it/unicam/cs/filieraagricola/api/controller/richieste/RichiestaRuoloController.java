package it.unicam.cs.filieraagricola.api.controller.richieste;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.gestore.RichiestaRuoloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/richieste")
public class RichiestaRuoloController {

    @Autowired
    private RichiestaRuoloService richiestaRuoloService;
    @Autowired
    private UserService userService;

    @PostMapping("/richiesta/ruoli")
    public ResponseEntity<String> aggiungiRichiestaRuolo(@RequestParam UserRole ruolo) {
        Users user = userService.getCurrentUser();
        if (!user.getRoles().contains(ruolo)) {
            if (!richiestaRuoloService.existsSameRichiesta(user, ruolo)) {
                richiestaRuoloService.aggiungiRichiesta(user.getId(), ruolo);
                return new ResponseEntity<>("Richiesta di ruolo " + ruolo + " da parte dell'utente: " + user.getId() + " inviata con successo.", HttpStatus.CREATED);
            } else return new ResponseEntity<>("Richiesta da parte di: " + user.getId() + " già effettuata per il ruolo: " + ruolo , HttpStatus.CONFLICT);
        } else return new ResponseEntity<>("L'utente: " + user.getId() + " già ha il ruolo di: " + ruolo, HttpStatus.CONFLICT);
    }

    @GetMapping("/attesa/ruoli")
    public ResponseEntity<Object> getRichiesteInAttesa() {
        return new ResponseEntity<>(richiestaRuoloService.getRichiesteInAttesa(), HttpStatus.OK);
    }

    @PutMapping("/processa/ruoli")
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
