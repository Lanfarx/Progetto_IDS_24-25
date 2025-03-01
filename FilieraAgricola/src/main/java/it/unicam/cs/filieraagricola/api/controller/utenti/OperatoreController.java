package it.unicam.cs.filieraagricola.api.controller.utenti;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import it.unicam.cs.filieraagricola.api.facades.RichiestaFacade;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaValidazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/operatore")
public class OperatoreController {

    @Autowired
    private RichiestaFacade richiestaFacade;

    @GetMapping("/richiesta-validazione")
    public ResponseEntity<List<RichiestaValidazione>> getMieRichiesteValidazione() {
        List<RichiestaValidazione> richieste = richiestaFacade.getMieRichiesteValidazione();
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }


}
