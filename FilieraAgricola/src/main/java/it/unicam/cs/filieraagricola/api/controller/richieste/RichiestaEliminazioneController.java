package it.unicam.cs.filieraagricola.api.controller.richieste;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.facades.RichiestaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class RichiestaEliminazioneController {

    @Autowired
    private RichiestaFacade richiestaFacade;

    @PostMapping("/autenticato/richiesta-eliminazione")
    public ResponseEntity<String> aggiungiRichiestaEliminazione(@RequestParam(required = false) String motivazione) {

        if (!richiestaFacade.existsSameRichiestaEliminazione(motivazione)) {
            richiestaFacade.aggiungiRichiestaEliminazione(motivazione);
            return new ResponseEntity<>("Richiesta di eliminazione inviata con successo.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Richiesta di eliminazione già effettuata", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/richieste/eliminazione/attesa")
    public ResponseEntity<Object> getRichiesteEliminazioneInAttesa() {
        return new ResponseEntity<>(richiestaFacade.getRichiesteEliminazioneInAttesa(), HttpStatus.OK);
    }

    @PutMapping("/richieste/eliminazione/processa")
    public ResponseEntity<Object> processaRichiestaEliminazione(@RequestParam Integer id, @RequestParam boolean approvato) {
        if (richiestaFacade.existsRichiestaEliminazione(id)) {
            RichiestaEliminazione richiesta = richiestaFacade.getRichiestaEliminazione(id).get();
            StatoContenuto statoContenuto = richiesta.getStato();
            if (statoContenuto == StatoContenuto.ATTESA) {
                richiestaFacade.processaRichiestaEliminazione(id, approvato);
                return new ResponseEntity<>("Richiesta di eliminazione " + id + (approvato ? " accettata e utente: " + richiesta.getUser().getId() + " eliminato" : " rifiutata"), HttpStatus.OK);
            } else {
                return ResponseEntity.status(409).body("Richiesta già processata con esito: " + statoContenuto);
            }
        } else {
            return ResponseEntity.status(404).body("Richiesta " + id + " non trovata");
        }
    }
}