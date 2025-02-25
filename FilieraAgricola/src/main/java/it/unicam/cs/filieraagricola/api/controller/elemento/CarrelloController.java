package it.unicam.cs.filieraagricola.api.controller.elemento;

import it.unicam.cs.filieraagricola.api.facades.CarrelloFacade;
import it.unicam.cs.filieraagricola.api.services.carrello.PaymentManager;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.Ordine;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
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
    private CarrelloFacade carrelloFacade;
    @Autowired
    private PaymentManager paymentManager;

    @PostMapping("/aggiungi")
    public ResponseEntity<String> aggiungiElementoAlCarrello(@RequestParam int id, @RequestParam int quantita) {
        if(carrelloFacade.existsElementoAndValido(id)){
            Elemento elemento = (Elemento) carrelloFacade.getElemento(id).get();
            if(quantita >= 1) {
                if (carrelloFacade.checkDisponibilita(elemento, quantita)) {
                    carrelloFacade.aggiungiAlCarrello(elemento, quantita);
                    return new ResponseEntity<>("Elemento aggiunto al carrello", HttpStatus.OK);
                } else return new ResponseEntity<>("Elemento non disponibile in quantità sufficiente (Max: " + elemento.getQuantita() + ")" , HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Inserire almeno quantita da aggiungere pari a 1", HttpStatus.OK);
        } else return new ResponseEntity<>("Elemento non esiste o non è valido", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/rimuovi")
    public ResponseEntity<String> rimuoviElementoDalCarrello(@RequestParam int id, @RequestParam int quantita) {
        if(carrelloFacade.existsElementoAndValido(id)){
            Elemento elemento = (Elemento) carrelloFacade.getElemento(id).get();
            if(quantita >= 1) {
                if (carrelloFacade.contieneElemento(elemento)) {
                    carrelloFacade.rimuoviDalCarrello(elemento, quantita);
                    return new ResponseEntity<>("Elemento rimosso dal carrello", HttpStatus.OK);
                } else return new ResponseEntity<>("Elemento non presente nel carrello", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Inserire almeno quantita da rimuovere pari a 1", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Elemento non esiste o non è valido", HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<Carrello> getCarrello() {
        Carrello carrello = carrelloFacade.getCarrello();
        return new ResponseEntity<>(carrello, HttpStatus.OK);
    }

    @GetMapping("/ordini")
    public ResponseEntity<List<Ordine>> getOrdini() {
        List<Ordine> ordini = carrelloFacade.getOrdini();
        return new ResponseEntity<>(ordini, HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Object> checkout(@RequestParam String metodo, @RequestParam String datiPagamento) {
        Carrello carrello = carrelloFacade.getCarrello();
        if(!carrello.getElementi().isEmpty()){
            if(paymentManager.effettuaPagamento(metodo, datiPagamento)){
                Ordine ordine = carrelloFacade.creaOrdine();
                return new ResponseEntity<>(ordine,HttpStatus.OK);
            } else return new ResponseEntity<>("Pagamento non valido, l'ordine non può essere completato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Impossibile fare il checkout con un carrello vuoto",HttpStatus.BAD_REQUEST);
    }
}
