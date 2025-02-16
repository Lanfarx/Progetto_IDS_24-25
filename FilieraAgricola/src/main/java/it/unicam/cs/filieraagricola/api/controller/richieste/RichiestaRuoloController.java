package it.unicam.cs.filieraagricola.api.controller.richieste;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaRuoloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class RichiestaRuoloController {

    @Autowired
    private RichiestaRuoloService richiestaRuoloService;
    @Autowired
    private UserService userService;

    @PostMapping("/autenticato/richiesta-ruolo")
    public ResponseEntity<String> aggiungiRichiestaRuolo(@RequestParam UserRole ruolo) {
        Users user = userService.getCurrentUser();
        if (!user.getRoles().contains(ruolo)) {
            if (!richiestaRuoloService.existsSameRichiesta(user, ruolo)) {
                richiestaRuoloService.aggiungiRichiesta(user.getId(), ruolo);
                return new ResponseEntity<>("Richiesta di ruolo " + ruolo + " da parte dell'utente: " + user.getUsername() + " inviata con successo.", HttpStatus.CREATED);
            } else return new ResponseEntity<>("Richiesta da parte di: " + user.getUsername() + " già effettuata per il ruolo: " + ruolo , HttpStatus.CONFLICT);
        } else return new ResponseEntity<>("L'utente: " + user.getUsername() + " già ha il ruolo di: " + ruolo, HttpStatus.CONFLICT);
    }

    @GetMapping("/richieste/ruoli/attesa")
    public ResponseEntity<Object> getRichiesteRuoloInAttesa() {
        return new ResponseEntity<>(richiestaRuoloService.getRichiesteInAttesa(), HttpStatus.OK);
    }

    @PutMapping("/richieste/ruoli/processa")
    public ResponseEntity<Object> processaRichiestaRuolo(@RequestParam Integer id, @RequestParam boolean approvato) {
        if(richiestaRuoloService.existsRichiesta(id))
        {
            RichiestaRuolo richiesta = richiestaRuoloService.getRichiesta(id).get();
            StatoContenuto statoContenuto = richiesta.getStato();
            if(statoContenuto == StatoContenuto.ATTESA){
                richiestaRuoloService.processaRichiesta(id, approvato);
                return new ResponseEntity<>("Richiesta: " + id + (approvato ? " accettata" : " rifiutata"), HttpStatus.OK);
            } else return ResponseEntity.status(409).body("Richiesta già processata con esito: " + statoContenuto);
        } else {
            return ResponseEntity.status(404).body("Richiesta " + id + " Non Trovata");
        }
    }
}
