package it.unicam.cs.filieraagricola.api.controller.richieste;

import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import it.unicam.cs.filieraagricola.api.facades.RichiestaFacade;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.unicam.cs.filieraagricola.api.commons.utils.ResponseEntityUtil.unauthorizedResponse;

@RestController
@RequestMapping
public class RichiestaValidazioneController {

    @Autowired
    private RichiestaFacade richiestaFacade;

    @PostMapping("operatore/richiesta-validazione")
    public ResponseEntity<Object> aggiungiRichiestaValidazione(@RequestParam Integer id) {
        if (richiestaFacade.existsElementoAndAttesa(id)) {
            Elemento elemento = richiestaFacade.getElemento(id).get();
            if (richiestaFacade.isUserCurrentUser(elemento.getOperatore())) {
                if (!richiestaFacade.existsSameRichiestaValidazione(elemento)) {
                    richiestaFacade.aggiungiRichiestaValidazione(elemento);
                    return new ResponseEntity<>("Richiesta di validazione per l'elemento '" + elemento.getNome() + "' inviata con successo.", HttpStatus.CREATED);
                } else return new ResponseEntity<>("Esiste già una richiesta di validazione per l'elemento '" + elemento.getNome() + "'", HttpStatus.CONFLICT);
            } else return unauthorizedResponse();
        } else return new ResponseEntity<>("L'elemento in attesa: " + id + " non esiste", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/richieste/validazione/attesa")
    public ResponseEntity<Object> getRichiesteInAttesa() {
        return new ResponseEntity<>(richiestaFacade.getRichiesteValidazioneInAttesa(), HttpStatus.OK);
    }

    @PutMapping("/richieste/validazione/processa")
    public ResponseEntity<Object> processaRichiestaValidazione(@RequestParam Integer id, @RequestParam boolean approvato) {
        if (richiestaFacade.existsRichiestaValidazione(id)) {
            RichiestaValidazione richiesta = richiestaFacade.getRichiestaValidazione(id).get();
            StatoContenuto statoContenuto = richiesta.getStato();
            if (statoContenuto == StatoContenuto.ATTESA) {
                richiestaFacade.processaRichiestaValidazione(id, approvato);
                return new ResponseEntity<>("Richiesta di validazione " + id + (approvato ? " accettata." : " rifiutata."), HttpStatus.OK);
            } else return ResponseEntity.status(409).body("Richiesta già processata con esito: " + statoContenuto);
        } else return ResponseEntity.status(404).body("Richiesta " + id + " non trovata");
    }
}