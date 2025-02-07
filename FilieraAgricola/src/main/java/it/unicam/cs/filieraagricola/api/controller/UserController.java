package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.AbstractRichiestaService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaEliminazioneService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaRuoloService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaValidazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RichiestaEliminazioneService richiestaEliminazioneService;
    @Autowired
    RichiestaRuoloService richiestaRuoloService;
    @Autowired
    private RichiestaValidazioneService richiestaValidazioneService;

    @GetMapping("/richieste")
    public ResponseEntity<List<Richiesta>> getMieRichieste() {
        Users currentUser = userService.getCurrentUser();
        List<Richiesta> richieste = richiestaRuoloService.getRichiesteByUser(currentUser);
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }

    @GetMapping("/richieste/ruolo")
    public ResponseEntity<List<RichiestaRuolo>> getMieRichiesteRuolo() {
        Users currentUser = userService.getCurrentUser();
        List<RichiestaRuolo> richieste = richiestaRuoloService.getMieRichiesteRuolo(currentUser);
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }

    @GetMapping("/richieste/eliminazione")
    public ResponseEntity<List<RichiestaEliminazione>> getMieRichiesteEliminazione() {
        Users currentUser = userService.getCurrentUser();
        List<RichiestaEliminazione> richieste = richiestaEliminazioneService.getMieRichiesteEliminazione(currentUser);
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE_DI_TIPICITA')")
    @GetMapping("/richieste/validazione")
    public ResponseEntity<List<RichiestaValidazione>> getMieRichiesteValidazione() {
        Users currentUser = userService.getCurrentUser();
        List<RichiestaValidazione> richieste = richiestaValidazioneService.getMieRichiesteValidazione(currentUser);
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }
}
