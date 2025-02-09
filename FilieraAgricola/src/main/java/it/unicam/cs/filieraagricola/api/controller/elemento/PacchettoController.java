package it.unicam.cs.filieraagricola.api.controller.elemento;

import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.elemento.PacchettoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/operatore/distributore")
public class PacchettoController {
    @Autowired
    private PacchettoService pacchettoService;
    @Autowired
    private UserService userService;

    public PacchettoController(PacchettoService pacchettoService) {
        this.pacchettoService = pacchettoService;
    }


    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Benvenuto nella dashboard del Distributore");
    }


    @RequestMapping({"/pacchetti"})
    public ResponseEntity<Object> getPacchetti() {
        if(pacchettoService.getPacchetti().isEmpty()) {
            return new ResponseEntity<>("Nessun pacchetto trovato", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pacchettoService.getPacchetti(), HttpStatus.FOUND);
    }

    @RequestMapping({"/pacchetti/{id}"})
    public ResponseEntity<Object> GetPacchetto(@PathVariable("id") int id) {
        if(pacchettoService.getPacchetto(id) == null) {
            return new ResponseEntity<>("Nessun pacchetto trovato", HttpStatus.NOT_FOUND);
        }
        if(pacchettoService.getPacchetto(id).getOperatore() == userService.getCurrentUser()) {
            return new ResponseEntity<>(pacchettoService.getPacchetto(id), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Non sei autorizzato a vedere questo pacchetto", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping({"/pacchetti/aggiungi"})
    public ResponseEntity<Object> aggiungiPacchetto(@RequestBody Pacchetto pacchetto) {
        if(pacchettoService.aggiungiPacchetto(pacchetto)) {
            return new ResponseEntity<>("Pacchetto aggiunto", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Pacchetto già esistente", HttpStatus.CONFLICT);
    }

    @PostMapping({"/pacchetti/aggiungi/prodotto"})
    public ResponseEntity<Object> aggiungiProdotto(@RequestParam ("idPacchetto") int idPacchetto,
                                                    @RequestParam ("idProdotto") int idProdotto){
        if(pacchettoService.aggiungiProdotto(idPacchetto, idProdotto)) {
            return new ResponseEntity<>("Prodotto aggiunto al pacchetto", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto già esistente nel pacchetto", HttpStatus.CONFLICT);
    }

    @PostMapping({"/pacchetti/aggiungiconparametri"})
    public ResponseEntity<Object> aggiungiPacchettoWithParam(@RequestParam("nome") String nome,
                                                             @RequestParam("descrizione") String descrizione,
                                                             @RequestParam("ProdottiSet") Set<Integer> idProdottiSet){
        if(pacchettoService.aggiungiPacchettoWithParam(nome, descrizione, idProdottiSet)) {
            return new ResponseEntity<>("Pacchetto aggiunto", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Pacchetto già esistente", HttpStatus.CONFLICT);
    }


    @RequestMapping({"/pacchetti/elimina/{id}"})
    public ResponseEntity<Object> eliminaPacchetto(@PathVariable("id") int id) {
        if(pacchettoService.getPacchetto(id).getOperatore() != userService.getCurrentUser()) {
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.UNAUTHORIZED);
        }
        pacchettoService.eliminaPacchetto(id);
        return new ResponseEntity<>("Pacchetto eliminato", HttpStatus.OK);
    }

    @RequestMapping({"/pacchetti/elimina/prodotto/"})
    public ResponseEntity<Object> eliminaProdotto(@RequestParam("idPacchetto") int id,
                                                  @RequestParam("idProdotto") int idProdotto) {

        if(pacchettoService.getPacchetto(id).getOperatore() != userService.getCurrentUser()) {
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.UNAUTHORIZED);
        }
        if(pacchettoService.eliminaProdotto(id, idProdotto)){
            return new ResponseEntity<>("Prodotto eliminato", HttpStatus.OK);
        }
        return new ResponseEntity<>("Prodotto non trovato", HttpStatus.CONFLICT);
    }

    @RequestMapping(
            value = {"/pacchetti/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> aggiornaPacchetto(@RequestBody Pacchetto pacchetto) {

        if(pacchetto.getOperatore() != userService.getCurrentUser()) {
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.UNAUTHORIZED);
        }
        if(pacchettoService.aggiornaPacchetto(pacchetto)) {
            return new ResponseEntity<>("Pacchetto aggiornato", HttpStatus.OK);
        }
        return new ResponseEntity<>("Pacchetto non trovato", HttpStatus.CONFLICT);
    }
}