package it.unicam.cs.filieraagricola.api.controller.richieste;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.facades.RichiestaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class RichiestaRuoloController {

    @Autowired
    private RichiestaFacade richiestaFacade;

    @PostMapping("/autenticato/richiesta-ruolo")
    public ResponseEntity<String> aggiungiRichiestaRuolo(@RequestParam UserRole ruolo) {
        if (!richiestaFacade.currentUserAlreadyHasRuolo(ruolo)) {
            if (!richiestaFacade.existsSameRichiestaRuolo(ruolo)) {
                richiestaFacade.aggiungiRichiestaRuolo(ruolo);
                return new ResponseEntity<>("Richiesta di ruolo " + ruolo + " da parte dell'utente inviata con successo.", HttpStatus.CREATED);
            } else return new ResponseEntity<>("Richiesta già effettuata per il ruolo: " + ruolo , HttpStatus.CONFLICT);
        } else return new ResponseEntity<>("L'utente già ha il ruolo di: " + ruolo, HttpStatus.CONFLICT);
    }

    @GetMapping("/richieste/ruoli/attesa")
    public ResponseEntity<Object> getRichiesteRuoloInAttesa() {
        return new ResponseEntity<>(richiestaFacade.getRichiesteRuoloInAttesa(), HttpStatus.OK);
    }

    @PutMapping("/richieste/ruoli/processa")
    public ResponseEntity<Object> processaRichiestaRuolo(@RequestParam Integer id, @RequestParam boolean approvato) {
        if(richiestaFacade.existsRichiestaRuolo(id))
        {
            RichiestaRuolo richiesta = richiestaFacade.getRichiestaRuolo(id).get();
            StatoContenuto statoContenuto = richiesta.getStato();
            if(statoContenuto == StatoContenuto.ATTESA){
                richiestaFacade.processaRichiestaRuolo(id, approvato);
                return new ResponseEntity<>("Richiesta: " + id + (approvato ? " accettata" : " rifiutata"), HttpStatus.OK);
            } else return ResponseEntity.status(409).body("Richiesta già processata con esito: " + statoContenuto);
        } else {
            return ResponseEntity.status(404).body("Richiesta " + id + " Non Trovata");
        }
    }
}
