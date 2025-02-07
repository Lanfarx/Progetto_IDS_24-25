package it.unicam.cs.filieraagricola.api.controller.richieste;

import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.services.elemento.ElementoService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaValidazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/richieste")
public class RichiestaValidazioneController {

    @Autowired
    private RichiestaValidazioneService richiestaValidazioneService;
    @Autowired
    private UserService userService;
    @Autowired
    private ElementoService elementoService;

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE_DI_TIPICITA')")
    @PostMapping("operatore/richiesta-validazione")
    public ResponseEntity<String> aggiungiRichiestaValidazione(@RequestParam Integer id) {
        Users user = userService.getCurrentUser();
        if (elementoService.existsElemento(id)) {
            Elemento elemento = elementoService.getElemento(id).get();
            if (!richiestaValidazioneService.existsSameRichiesta(user, elemento)) {
                richiestaValidazioneService.aggiungiRichiesta(user.getId(), elemento);
                return new ResponseEntity<>("Richiesta di validazione per l'elemento '" + elemento.getNome() + "' inviata con successo.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Esiste già una richiesta di validazione per l'elemento '" + elemento.getNome() + "'", HttpStatus.CONFLICT);
            }
        } else return new ResponseEntity<>("L'elemento: " + id + " non esiste", HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('CURATORE')")
    @GetMapping("/attesa/validazione")
    public ResponseEntity<Object> getRichiesteInAttesa() {
        return new ResponseEntity<>(richiestaValidazioneService.getRichiesteInAttesa(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CURATORE')")
    @GetMapping("/processa/validazione")
    public ResponseEntity<Object> processaRichiestaValidazione(@RequestParam Integer id, @RequestParam boolean approvato) {
        if (richiestaValidazioneService.existsRichiesta(id)) {
            RichiestaValidazione richiesta = richiestaValidazioneService.getRichiesta(id).get();
            StatoRichiesta statoRichiesta = richiesta.getStato();
            if (statoRichiesta == StatoRichiesta.ATTESA) {
                richiestaValidazioneService.processaRichiesta(id, approvato);
                return new ResponseEntity<>("Richiesta di validazione " + id + (approvato ? " accettata." : " rifiutata."), HttpStatus.OK);
            } else {
                return ResponseEntity.status(409).body("Richiesta già processata con esito: " + statoRichiesta);
            }
        } else {
            return ResponseEntity.status(404).body("Richiesta " + id + " non trovata");
        }
    }
}