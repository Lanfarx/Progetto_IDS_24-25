package it.unicam.cs.filieraagricola.api.controller.richieste;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaEliminazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/richieste")
public class RichiestaEliminazioneController {

    @Autowired
    private RichiestaEliminazioneService richiestaEliminazioneService;
    @Autowired
    private UserService userService;

    @PostMapping("/richiesta/eliminazione")
    public ResponseEntity<String> aggiungiRichiestaEliminazione(@RequestParam(required = false) String motivazione) {
        Users user = userService.getCurrentUser();

        if (!richiestaEliminazioneService.existsSameRichiesta(user, motivazione)) {
            richiestaEliminazioneService.aggiungiRichiesta(user.getId(), motivazione);
            return new ResponseEntity<>("Richiesta di eliminazione da parte dell'utente: " + user.getId() + " inviata con successo.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Richiesta di eliminazione già effettuata da parte dell'utente: " + user.getId(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/attesa/eliminazione")
    public ResponseEntity<Object> getRichiesteEliminazioneInAttesa() {
        return new ResponseEntity<>(richiestaEliminazioneService.getRichiesteInAttesa(), HttpStatus.OK);
    }

    @PutMapping("/processa/eliminazione")
    public ResponseEntity<Object> processaRichiestaEliminazione(@RequestParam Integer id, @RequestParam boolean approvato) {
        if (richiestaEliminazioneService.existsRichiesta(id)) {
            RichiestaEliminazione richiesta = richiestaEliminazioneService.getRichiesta(id).get();
            StatoRichiesta statoRichiesta = richiesta.getStato();
            if (statoRichiesta == StatoRichiesta.ATTESA) {
                richiestaEliminazioneService.processaRichiesta(id, approvato);
                return new ResponseEntity<>("Richiesta di eliminazione " + id + (approvato ? " accettata e utente: " + richiesta.getUser().getId() + " eliminato" : " rifiutata"), HttpStatus.OK);
            } else {
                return ResponseEntity.status(409).body("Richiesta già processata con esito: " + statoRichiesta);
            }
        } else {
            return ResponseEntity.status(404).body("Richiesta " + id + " non trovata");
        }
    }
}