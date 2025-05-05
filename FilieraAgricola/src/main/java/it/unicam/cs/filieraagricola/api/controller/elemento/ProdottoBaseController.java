package it.unicam.cs.filieraagricola.api.controller.elemento;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.facades.ElementoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.unicam.cs.filieraagricola.api.commons.utils.ResponseEntityUtil.unauthorizedResponse;

@RestController
@RequestMapping("/operatore/produttore")
public class ProdottoBaseController {

    @Autowired
    private ElementoFacade elementoFacade;

    @RequestMapping({"/prodottibase"})
    public ResponseEntity<Object> getProdottiBase() {
        if(!elementoFacade.getAllProdottiBase().isEmpty()){
            return new ResponseEntity<>(elementoFacade.getAllProdottiBase(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Non esistono prodotti base", HttpStatus.NOT_FOUND);
    }

    @RequestMapping({"/prodottibase/{id}"})
    public ResponseEntity<Object> getProdottoBaseById(@PathVariable int id) {
        if(elementoFacade.getProdottoBaseById(id) != null){
            if(elementoFacade.isUserCurrentUser(elementoFacade.getProdottoBaseById(id).getOperatore())) {
                return new ResponseEntity<>(elementoFacade.getProdottoBaseById(id), HttpStatus.FOUND);
            } else return unauthorizedResponse();
        }
        return new ResponseEntity<>("Prodotto base non esistente", HttpStatus.NOT_FOUND);
    }

    @PostMapping({"/prodottibase/aggiungi"})
    public ResponseEntity<Object> aggiungiProdottoBase(@RequestBody ProdottoBase prodotto) {
        //TODO aggiungi condizione di esistenza
        if(elementoFacade.aggiungiProdottoBase(prodotto)){
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto già esistente", HttpStatus.CONFLICT);
    }

    @PostMapping({"prodottibase/aggiungiconparametri"})
    public ResponseEntity<Object> aggiungiProdottoBase(@RequestParam("nome") String nome,
                                                      @RequestParam("metodiDiColtivazione") String metodiDiColtivazione,
                                                      @RequestParam("certificazioni") String certificazioni,
                                                       @RequestParam("descrizione") String descrizione,
                                                       @RequestParam("prezzo") double prezzo,
                                                       @RequestParam("categoria") String categoria) {

        if(elementoFacade.aggiungiProdottoBase(nome, metodiDiColtivazione, certificazioni, descrizione, prezzo, categoria)){
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto già esistente", HttpStatus.CONFLICT);
    }

    @DeleteMapping({"/prodottibase/elimina"})
    public ResponseEntity<Object> eliminaProdotto(@RequestParam int id) {
        if(elementoFacade.getProdottoBaseById(id) != null) {
            if (!elementoFacade.isUserCurrentUser(elementoFacade.getProdottoBaseById(id).getOperatore())) {
                return unauthorizedResponse();
            }
            elementoFacade.deleteProdottoBase(id);
            return new ResponseEntity<>("Prodotto eliminato", HttpStatus.OK);
        } else return new ResponseEntity<>("Prodotto non esistente", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottibase/aggiorna")
    public ResponseEntity<Object> aggiornaProdottoBase(@RequestBody ProdottoBase prodottoBase) {
        if(elementoFacade.existsProdottoBase(prodottoBase.getId())){
            ProdottoBase prodotto = elementoFacade.getProdottoBaseById(prodottoBase.getId());
            if (!elementoFacade.isUserCurrentUser(prodotto.getOperatore())) {
                return unauthorizedResponse();
            }
            if (elementoFacade.aggiornaProdottoBase(prodottoBase)) {
                return new ResponseEntity<>("Prodotto aggiornato", HttpStatus.FOUND);
            }
            return new ResponseEntity<>("Prodotto non aggiornato", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Prodotto non esistente", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottibase/aggiorna-quantita")
    public ResponseEntity<Object> aggiornaQuantitaProdotto(@RequestParam int id, @RequestParam int quantita) {
        ProdottoBase prodotto = elementoFacade.getProdottoBaseById(id);
        if (prodotto != null) {
            if (!elementoFacade.isUserCurrentUser(prodotto.getOperatore())) {
                return unauthorizedResponse();
            }
            if (elementoFacade.aggiornaQuantitaProdotto(id, quantita)) {
                return new ResponseEntity<>("Quantità aggiornata", HttpStatus.OK);
            }
            return new ResponseEntity<>("Impossibile aggiornare la quantità", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Prodotto non trovato", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottibase/rimuovi-quantita")
    public ResponseEntity<Object> rimuoviQuantitaProdotto(@RequestParam int id, @RequestParam int quantita) {
        ProdottoBase prodotto = elementoFacade.getProdottoBaseById(id);
        if (prodotto != null) {
            if (!elementoFacade.isUserCurrentUser(prodotto.getOperatore())) {
                return unauthorizedResponse();
            }
            if (elementoFacade.riduciQuantitaProdotto(id, quantita)) {
                return new ResponseEntity<>("Quantità ridotta", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Quantità insufficiente per rimuovere", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Prodotto non trovato", HttpStatus.NOT_FOUND);
    }
}
