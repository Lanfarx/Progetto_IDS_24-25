package it.unicam.cs.filieraagricola.api.controller.elemento;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Categoria;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.facades.ElementoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.unicam.cs.filieraagricola.api.commons.utils.ResponseEntityUtil.unauthorizedResponse;

@RestController
@RequestMapping("/operatore/trasformatore")
public class ProdottoTrasformatoController {

    @Autowired
    private ElementoFacade elementoFacade;

    @RequestMapping({"/prodottitrasformati"})
    public ResponseEntity<Object> getAllProdottiTrasformati() {
        if(!elementoFacade.getAllProdottiTrasformati().isEmpty()){
            return new ResponseEntity<>(elementoFacade.getAllProdottiTrasformati(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Nessun prodotto trasformato esistente",HttpStatus.NOT_FOUND);
    }

    @RequestMapping({"/prodottitrasformati/{id}"})
    public ResponseEntity<Object> getProdottoTrasformatoById(@PathVariable   int id) {
        if(elementoFacade.getProdottoTrasformatoById(id) != null ){
            if(elementoFacade.isUserCurrentUser(elementoFacade.getProdottoTrasformatoById(id).getOperatore())) {
                return new ResponseEntity<>(elementoFacade.getProdottoTrasformatoById(id), HttpStatus.FOUND);
            } else {
                return unauthorizedResponse();
            }
        }
        return new ResponseEntity<>("Prodotto trasformato non esistente", HttpStatus.NOT_FOUND);
    }

    @PostMapping({"/prodottitrasformati/aggiungi"})
    public ResponseEntity<Object> aggiungiProdottoTrasformato(@RequestBody ProdottoTrasformato prodotto) {
        ProdottoBase prodottoBase = elementoFacade.getProdottoBaseById(prodotto.getProdottoBase().getId());
        if (prodottoBase == null) {
            return new ResponseEntity<>("Prodotto base non trovato", HttpStatus.NOT_FOUND);
        }
        if (prodottoBase.getStatorichiesta() != StatoContenuto.ACCETTATA) {
            return new ResponseEntity<>("Il prodotto base associato deve essere stato precedentemente validato", HttpStatus.BAD_REQUEST);
        }

        if(elementoFacade.aggiungiProdottoTrasformato(prodotto)){
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto trasformato esistente", HttpStatus.CONFLICT);
    }

    @PostMapping({"prodottitrasformati/aggiungiconparametri"})
    public ResponseEntity<Object> aggiungiProdottoTrasformatoConParametri(@RequestParam("nome") String nome,
                                                      @RequestParam("processoTrasformazione") String processoTrasformazione,
                                                      @RequestParam("certificazioni") String certificazioni,
                                                      @RequestParam("prodottoBase") int IDprodottoBase,
                                                      @RequestParam("descrizione") String descrizione,
                                                      @RequestParam("prezzo") double prezzo,
                                                      @RequestParam("categoria") String categoria)
    {
        ProdottoBase prodottoBase = elementoFacade.getProdottoBaseById(IDprodottoBase);
        if (prodottoBase == null) {
            return new ResponseEntity<>("Prodotto base non trovato", HttpStatus.NOT_FOUND);
        }
        if (prodottoBase.getStatorichiesta() != StatoContenuto.ACCETTATA) {
            return new ResponseEntity<>("Il prodotto base associato deve essere stato precedentemente validato", HttpStatus.BAD_REQUEST);
        }
        if(!elementoFacade.existsSameCategoria(categoria)){
            return new ResponseEntity<>("Categoria non esistente, Categorie esistenti: " + elementoFacade.getAllCategorie(), HttpStatus.NOT_FOUND);
        }
        Categoria cat = (Categoria) elementoFacade.getCategoriaByNome(categoria).get();
        if(elementoFacade.aggiungiProdottoTrasformato(nome, processoTrasformazione, certificazioni, IDprodottoBase, descrizione, prezzo, cat)){
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto trasformato esistente", HttpStatus.CONFLICT);
    }

    @RequestMapping({"/prodottitrasformati/elimina"})
    public ResponseEntity<Object> eliminaProdottoTrasformato(@RequestParam int id) {
        if (elementoFacade.getProdottoTrasformatoById(id) != null) {
            if (elementoFacade.isUserCurrentUser(elementoFacade.getProdottoTrasformatoById(id).getOperatore())) {
                return unauthorizedResponse();
            }
            elementoFacade.deleteProdottoTrasformato(id);
            return new ResponseEntity<>("Prodotto trasformato eliminato", HttpStatus.OK);
        } else return new ResponseEntity<>("Prodotto non esistente", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottitrasformati/aggiorna")
    public ResponseEntity<Object> aggiornaProdottoTrasformato(@RequestBody ProdottoTrasformato prodottoTrasformato) {
        if (elementoFacade.existsProdottoTrasformato(prodottoTrasformato.getId())) {
            ProdottoTrasformato prodotto = elementoFacade.getProdottoTrasformatoById(prodottoTrasformato.getId());
            if (!elementoFacade.isUserCurrentUser(prodotto.getOperatore())) {
                return unauthorizedResponse();
            }
            if (elementoFacade.aggiornaProdottoTrasformato(prodottoTrasformato)) {
                return new ResponseEntity<>("Prodotto trasformato aggiornato", HttpStatus.FOUND);
            }
            return new ResponseEntity<>("Prodotto non aggiornato", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Prodotto non esistente", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottitrasformati/aggiorna-quantita")
    public ResponseEntity<Object> aggiornaQuantitaProdotto(@RequestParam int id, @RequestParam int quantita) {
        ProdottoTrasformato prodotto = elementoFacade.getProdottoTrasformatoById(id);
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

    @PutMapping("/prodottitrasformati/rimuovi-quantita")
    public ResponseEntity<Object> rimuoviQuantitaProdotto(@RequestParam int id, @RequestParam int quantita) {
        ProdottoTrasformato prodotto = elementoFacade.getProdottoTrasformatoById(id);
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
