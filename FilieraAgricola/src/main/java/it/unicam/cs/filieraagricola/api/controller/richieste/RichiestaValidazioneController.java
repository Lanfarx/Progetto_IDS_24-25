package it.unicam.cs.filieraagricola.api.controller.richieste;

import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.services.elemento.ElementoService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaValidazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.unicam.cs.filieraagricola.api.commons.ResponseEntityUtil.unauthorizedResponse;

@RestController
@RequestMapping
public class RichiestaValidazioneController {

    @Autowired
    private RichiestaValidazioneService richiestaValidazioneService;
    @Autowired
    private UserService userService;
    @Autowired
    private ElementoService<Elemento> elementoService;

    @PostMapping("operatore/richiesta-validazione")
    public ResponseEntity<Object> aggiungiRichiestaValidazione(@RequestParam Integer id) {
        Users user = userService.getCurrentUser();
        if (elementoService.existsElemento(id)) {
            Elemento elemento = elementoService.getElemento(id).get();
            if (elemento.getOperatore().equals(user)) {
                if (!richiestaValidazioneService.existsSameRichiesta(user, elemento)) {
                    richiestaValidazioneService.aggiungiRichiesta(user.getId(), elemento);
                    return new ResponseEntity<>("Richiesta di validazione per l'elemento '" + elemento.getNome() + "' inviata con successo.", HttpStatus.CREATED);
                } else return new ResponseEntity<>("Esiste già una richiesta di validazione per l'elemento '" + elemento.getNome() + "'", HttpStatus.CONFLICT);
            } else return unauthorizedResponse();
        } else return new ResponseEntity<>("L'elemento: " + id + " non esiste", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/richieste/validazione/attesa")
    public ResponseEntity<Object> getRichiesteInAttesa() {
        return new ResponseEntity<>(richiestaValidazioneService.getRichiesteInAttesa(), HttpStatus.OK);
    }

    @PutMapping("/richieste/validazione/processa")
    public ResponseEntity<Object> processaRichiestaValidazione(@RequestParam Integer id, @RequestParam boolean approvato) {
        if (richiestaValidazioneService.existsRichiesta(id)) {
            RichiestaValidazione richiesta = richiestaValidazioneService.getRichiesta(id).get();
            StatoContenuto statoContenuto = richiesta.getStato();
            if (statoContenuto == StatoContenuto.ATTESA) {
                richiestaValidazioneService.processaRichiesta(id, approvato);
                return new ResponseEntity<>("Richiesta di validazione " + id + (approvato ? " accettata." : " rifiutata."), HttpStatus.OK);
            } else return ResponseEntity.status(409).body("Richiesta già processata con esito: " + statoContenuto);
        } else return ResponseEntity.status(404).body("Richiesta " + id + " non trovata");
    }
}