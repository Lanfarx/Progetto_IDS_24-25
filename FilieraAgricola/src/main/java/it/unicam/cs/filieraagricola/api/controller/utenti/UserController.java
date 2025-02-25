package it.unicam.cs.filieraagricola.api.controller.utenti;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.entities.carrello.Ordine;
import it.unicam.cs.filieraagricola.api.entities.elemento.*;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.facades.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserFacade userFacade;

    @GetMapping("/autenticato/me")
    public ResponseEntity<Users> getMyInfo() {
        Users currentUser = userFacade.getCurrentUser();
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @PutMapping("/autenticato/me")
    public ResponseEntity<String> modificaCredenziali(@RequestParam String username, @RequestParam String password) {
        Users user = userFacade.getCurrentUser();
        if (!user.getUsername().equals(username) && !user.getPassword().equals(password)) {
            if (!userFacade.existsUserByUsername(username)) {
                userFacade.modificaCredenziali(user, username, password);
                return new ResponseEntity<>("Utente aggiornato con username: " + username + " e password: " + password, HttpStatus.OK);
            } else return new ResponseEntity<>("Username: " + username + " già presente", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Credenziali inserite già presenti", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/autenticato/attivita")
    public ResponseEntity<Object> getAttivita() {
        return new ResponseEntity<>(userFacade.getAllAttivita(), HttpStatus.OK);
    }

    @GetMapping("/autenticato/visite")
    public ResponseEntity<Object> getVisite() {
        return new ResponseEntity<>(userFacade.getAllVisite(), HttpStatus.OK);
    }

    @GetMapping("/autenticato/eventi")
    public ResponseEntity<Object> getEventi() {
        return new ResponseEntity<>(userFacade.getAllEventi(), HttpStatus.OK);
    }

    @GetMapping("/autenticato/attivita/prenotazione")
    public ResponseEntity<Object> getPrenotazioni() {
        return new ResponseEntity<>(userFacade.getAllPrenotazioni(), HttpStatus.OK);
    }

    @GetMapping("/autenticato/ordini")
    public ResponseEntity<List<Ordine>> getOrdini() {
        List<Ordine> ordini = userFacade.getOrdini();
        return new ResponseEntity<>(ordini, HttpStatus.OK);
    }

    @PostMapping("/autenticato/attivita/prenotazione")
    public ResponseEntity<String> prenotazione(@RequestParam Integer id) {
        Users currentUser = userFacade.getCurrentUser();
        if (userFacade.existsAttivita(id)) {
            Visita visita = userFacade.getVisitaOEventoById(id).get();
            if (userFacade.controllaPrenotazione(visita, currentUser)) {
                if (userFacade.checkData(visita.getData())) {
                    userFacade.aggiungiPrenotazione(visita, currentUser);
                    return new ResponseEntity<>("Prenotazione effettuata con successo", HttpStatus.OK);
                } else
                    return new ResponseEntity<>("È possibile prenotarsi solo ad attività non già precedentemente svolte", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("L'utente è gia prenotato a questa attività", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("L'attività non esiste", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/autenticato/attivita/prenotazione")
    public ResponseEntity<String> rimuoviPrenotazione(@RequestParam Integer id) {
        Users currentUser = userFacade.getCurrentUser();
        if (userFacade.existsAttivita(id)) {
            Visita visita = userFacade.getVisitaOEventoById(id).get();
            if (!userFacade.controllaPrenotazione(visita, currentUser)) {
                if (userFacade.checkData(visita.getData())) {
                    userFacade.eliminaPrenotazione(visita, currentUser);
                    return new ResponseEntity<>("Prenotazione eliminata con successo", HttpStatus.OK);
                } else
                    return new ResponseEntity<>("È possibile togliere la prenotazione solo ad attività non già precedentemente svolte", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("L'utente non è prenotato a questa attività", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("L'attività non esiste", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/elementi")
    public ResponseEntity<List<Elemento>> getElementi() {
        return new ResponseEntity<>(userFacade.getElementiValidi(), HttpStatus.OK);
    }

    @GetMapping("/elementi/prodotti")
    public ResponseEntity<List<Prodotto>> getProdotti() {
        return new ResponseEntity<>(userFacade.getProdottiValidi(), HttpStatus.OK);
    }

    @GetMapping("/elementi/pacchetti")
    public ResponseEntity<List<Pacchetto>> getPacchetti() {
        return new ResponseEntity<>(userFacade.getPacchettiValidi(), HttpStatus.OK);
    }

    @GetMapping("/elementi/prodotti-base")
    public ResponseEntity<List<ProdottoBase>> getAllProdottiBase() {
        return new ResponseEntity<>(userFacade.getProdottiBaseValidi(), HttpStatus.OK);
    }

    @GetMapping("/elementi/prodotti-trasformati")
    public ResponseEntity<List<ProdottoTrasformato>> getAllProdottiTrasformati() {
        return new ResponseEntity<>(userFacade.getProdottiTrasformatiValidi(), HttpStatus.OK);
    }

    @GetMapping("/autenticato/richieste")
    public ResponseEntity<List<Richiesta>> getMieRichieste() {
        Users currentUser = userFacade.getCurrentUser();
        List<Richiesta> richieste = userFacade.getRichiesteByUser(currentUser);
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }

    @GetMapping("/autenticato/richiesta-ruolo")
    public ResponseEntity<List<RichiestaRuolo>> getMieRichiesteRuolo() {
        Users currentUser = userFacade.getCurrentUser();
        List<RichiestaRuolo> richieste = userFacade.getRichiesteRuolo(currentUser);
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }

    @GetMapping("/autenticato/richiesta-eliminazione")
    public ResponseEntity<List<RichiestaEliminazione>> getMieRichiesteEliminazione() {
        Users currentUser = userFacade.getCurrentUser();
        List<RichiestaEliminazione> richieste = userFacade.getRichiesteEliminazione(currentUser);
        return new ResponseEntity<>(richieste, HttpStatus.OK);
    }
}
