package it.unicam.cs.filieraagricola.api.controller.utenti;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.entities.elemento.*;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.elemento.*;
import it.unicam.cs.filieraagricola.api.services.AttivitaService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaEliminazioneService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaRuoloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AttivitaService attivitaService;
    @Autowired
    private ElementoService<Elemento> elementoService;
    @Autowired
    private ProdottoService<Prodotto> prodottoService;
    @Autowired
    private PacchettoService pacchettoService;
    @Autowired
    private ProdottoBaseService prodottoBaseService;
    @Autowired
    private ProdottoTrasformatoService prodottoTrasformatoService;
    @Autowired
    private RichiestaEliminazioneService richiestaEliminazioneService;
    @Autowired
    private RichiestaRuoloService richiestaRuoloService;

    @GetMapping("/autenticato/me")
    public ResponseEntity<Users> getMyInfo() {
        Users currentUser = userService.getCurrentUser();
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @PutMapping("/autenticato/me")
    public ResponseEntity<String> modificaCredenziali(@RequestParam String username, @RequestParam String password) {
        Users user = userService.getCurrentUser();
        if (!user.getUsername().equals(username) && !user.getPassword().equals(password)) {
            if (!userService.existsUserByUsername(username)) {
                userService.modificaCredenziali(user, username, password);
                return new ResponseEntity<>("Utente aggiornato con username: " + username + " e password: " + password, HttpStatus.OK);
            } else return new ResponseEntity<>("Username: " + username + " già presente", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Credenziali inserite già presenti", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/autenticato/attivita")
    public ResponseEntity<Object> getAttivita() {
        return new ResponseEntity<>(attivitaService.getAllAttivita(), HttpStatus.OK);
    }

    @GetMapping("/autenticato/visite")
    public ResponseEntity<Object> getVisite() {
        return new ResponseEntity<>(attivitaService.getAllVisite(), HttpStatus.OK);
    }

    @GetMapping("/autenticato/eventi")
    public ResponseEntity<Object> getEventi() {
        return new ResponseEntity<>(attivitaService.getAllEventi(), HttpStatus.OK);
    }

    @GetMapping("/autenticato/attivita/prenotazione")
    public ResponseEntity<Object> getPrenotazioni() {
        return new ResponseEntity<>(attivitaService.getAllPrenotazioni(userService.getCurrentUser()), HttpStatus.OK);
    }

    @PostMapping("/autenticato/attivita/prenotazione")
    public ResponseEntity<String> prenotazione(@RequestParam Integer id) {
        Users currentUser = userService.getCurrentUser();
        if (attivitaService.existsAttivita(id)) {
            Visita visita = attivitaService.getVisitaOEventoById(id).get();
            if (attivitaService.controllaPrenotazione(visita, currentUser)) {
                if (attivitaService.checkData(visita.getData())) {
                    attivitaService.aggiungiPrenotazione(visita, currentUser);
                    return new ResponseEntity<>("Prenotazione effettuata con successo", HttpStatus.OK);
                } else
                    return new ResponseEntity<>("È possibile prenotarsi solo ad attività non già precedentemente svolte", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("L'utente è gia prenotato a questa attività", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("L'attività non esiste", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/autenticato/attivita/prenotazione")
    public ResponseEntity<String> rimuoviPrenotazione(@RequestParam Integer id) {
        Users currentUser = userService.getCurrentUser();
        if (attivitaService.existsAttivita(id)) {
            Visita visita = attivitaService.getVisitaOEventoById(id).get();
            if (!attivitaService.controllaPrenotazione(visita, currentUser)) {
                if (attivitaService.checkData(visita.getData())) {
                    attivitaService.eliminaPrenotazione(visita, currentUser);
                    return new ResponseEntity<>("Prenotazione eliminata con successo", HttpStatus.OK);
                } else
                    return new ResponseEntity<>("È possibile togliere la prenotazione solo ad attività non già precedentemente svolte", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("L'utente non è prenotato a questa attività", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("L'attività non esiste", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/elementi")
    public ResponseEntity<List<Elemento>> getElementi() {
        return new ResponseEntity<>(elementoService.getElementiValidi(), HttpStatus.OK);
    }

    @GetMapping("/elementi/prodotti")
    public ResponseEntity<List<Prodotto>> getProdotti() {
        return new ResponseEntity<>(prodottoService.getProdottiValidi(), HttpStatus.OK);
    }

    @GetMapping("/elementi/pacchetti")
    public ResponseEntity<List<Pacchetto>> getPacchetti() {
        return new ResponseEntity<>(pacchettoService.getAllPacchettiValidi(), HttpStatus.OK);
    }

    @GetMapping("/elementi/prodotti-base")
    public ResponseEntity<List<ProdottoBase>> getAllProdottiBase() {
        return new ResponseEntity<>(prodottoBaseService.getAllProdottiBaseValidi(), HttpStatus.OK);
    }

    @GetMapping("/elementi/prodotti-trasformati")
    public ResponseEntity<List<ProdottoTrasformato>> getAllProdottiTrasformati() {
        return new ResponseEntity<>(prodottoTrasformatoService.getAllProdottiTrasformatiValidi(), HttpStatus.OK);
    }

    @GetMapping("/autenticato/richieste")
    public ResponseEntity<List<Richiesta>> getMieRichieste() {
        Users currentUser = userService.getCurrentUser();
        List<Richiesta> richieste = richiestaRuoloService.getRichiesteByUser(currentUser);
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }

    @GetMapping("/autenticato/richiesta-ruolo")
    public ResponseEntity<List<RichiestaRuolo>> getMieRichiesteRuolo() {
        Users currentUser = userService.getCurrentUser();
        List<RichiestaRuolo> richieste = richiestaRuoloService.getMieRichiesteRuolo(currentUser);
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }

    @GetMapping("/autenticato/richiesta-eliminazione")
    public ResponseEntity<List<RichiestaEliminazione>> getMieRichiesteEliminazione() {
        Users currentUser = userService.getCurrentUser();
        List<RichiestaEliminazione> richieste = richiestaEliminazioneService.getMieRichiesteEliminazione(currentUser);
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }
}
