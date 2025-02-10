package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.services.carrello.PaymentManager;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.Ordine;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.carrello.CarrelloService;
import it.unicam.cs.filieraagricola.api.services.carrello.OrdineService;
import it.unicam.cs.filieraagricola.api.services.elemento.ElementoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/autenticato/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;
    @Autowired
    private ElementoService elementoService;
    @Autowired
    private OrdineService ordineService;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentManager paymentManager;

    @PostMapping("/aggiungi")
    public ResponseEntity<String> aggiungiElementoAlCarrello(@RequestParam int id, @RequestParam int quantita) {
        if(elementoService.existsElementoAndValido(id)){
            Elemento elemento = (Elemento) elementoService.getElemento(id).get();
            if(quantita >= 1) {
                if (elementoService.checkDisponibilita(elemento, quantita)) {
                    Users currentUser = userService.getCurrentUser();
                    carrelloService.aggiungiAlCarrello(currentUser, elemento, quantita);
                    return new ResponseEntity<>("Elemento aggiunto al carrello", HttpStatus.OK);
                } else return new ResponseEntity<>("Elemento non disponibile in quantità sufficiente (Max: " + elemento.getQuantita() + ")" , HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Inserire almeno quantita da aggiungere pari a 1", HttpStatus.OK);
        } else return new ResponseEntity<>("Elemento non esiste o non è valido", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/rimuovi")
    public ResponseEntity<String> rimuoviElementoDalCarrello(@RequestParam int id, @RequestParam int quantita) {
        if(elementoService.existsElementoAndValido(id)){
            Elemento elemento = (Elemento) elementoService.getElemento(id).get();
            Users currentUser = userService.getCurrentUser();
            Carrello carrello = carrelloService.getCarrello(currentUser);
            if(quantita >= 1) {
                if (carrelloService.contieneElemento(carrello, elemento)) {
                    carrelloService.rimuoviDalCarrello(currentUser, elemento, quantita);
                    return new ResponseEntity<>("Elemento rimosso dal carrello", HttpStatus.OK);
                } else return new ResponseEntity<>("Elemento non presente nel carrello", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Inserire almeno quantita da rimuovere pari a 1", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Elemento non esiste o non è valido", HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<Carrello> getCarrello() {
        Users currentUser = userService.getCurrentUser();
        Carrello carrello = carrelloService.getCarrello(currentUser);
        return new ResponseEntity<>(carrello, HttpStatus.OK);
    }

    @GetMapping("/ordini")
    public ResponseEntity<List<Ordine>> getOrdini() {
        Users currentUser = userService.getCurrentUser();
        List<Ordine> ordini = ordineService.getOrdini(currentUser);
        return new ResponseEntity<>(ordini, HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Object> checkout(@RequestParam String cartaDiCredito) {
        Users currentUser = userService.getCurrentUser();
        Carrello carrello = carrelloService.getCarrello(currentUser);
        if(!carrello.getElementi().isEmpty()){
            if(paymentManager.verificaPagamento(cartaDiCredito)){
                Ordine ordine = ordineService.creaOrdine(carrello);
                return new ResponseEntity<>(ordine,HttpStatus.OK);
            } else return new ResponseEntity<>("Pagamento non valido, l'ordine non può essere completato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Impossibile fare il checkout con un carrello vuoto",HttpStatus.BAD_REQUEST);
    }
}
