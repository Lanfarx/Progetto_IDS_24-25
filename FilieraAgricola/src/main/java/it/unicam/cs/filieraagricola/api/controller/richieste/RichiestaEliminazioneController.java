package it.unicam.cs.filieraagricola.api.controller.richieste;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaEliminazioneService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class RichiestaEliminazioneController {

    @Autowired
    private RichiestaEliminazioneService richiestaEliminazioneService;
    @Autowired
    private UserService userService;

    @PostMapping("/autenticato/richiesta-eliminazione")
    public ResponseEntity<String> aggiungiRichiestaEliminazione(@RequestParam(required = false) String motivazione) {
        Users user = userService.getCurrentUser();

        if (!richiestaEliminazioneService.existsSameRichiesta(user, motivazione)) {
            richiestaEliminazioneService.aggiungiRichiesta(user.getId(), motivazione);
            return new ResponseEntity<>("Richiesta di eliminazione da parte dell'utente: " + user.getUsername() + " inviata con successo.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Richiesta di eliminazione già effettuata da parte dell'utente: " + user.getUsername(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/richieste/eliminazione/attesa")
    public ResponseEntity<Object> getRichiesteEliminazioneInAttesa() {
        return new ResponseEntity<>(richiestaEliminazioneService.getRichiesteInAttesa(), HttpStatus.OK);
    }

    @PutMapping("/richieste/eliminazione/processa")
    public ResponseEntity<Object> processaRichiestaEliminazione(@RequestParam Integer id, @RequestParam boolean approvato) {
        if (richiestaEliminazioneService.existsRichiesta(id)) {
            RichiestaEliminazione richiesta = richiestaEliminazioneService.getRichiesta(id).get();
            StatoContenuto statoContenuto = richiesta.getStato();
            if (statoContenuto == StatoContenuto.ATTESA) {
                richiestaEliminazioneService.processaRichiesta(id, approvato);
                return new ResponseEntity<>("Richiesta di eliminazione " + id + (approvato ? " accettata e utente: " + richiesta.getUser().getId() + " eliminato" : " rifiutata"), HttpStatus.OK);
            } else {
                return ResponseEntity.status(409).body("Richiesta già processata con esito: " + statoContenuto);
            }
        } else {
            return ResponseEntity.status(404).body("Richiesta " + id + " non trovata");
        }
    }
}