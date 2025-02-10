package it.unicam.cs.filieraagricola.api.controller.elemento;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Categoria;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.elemento.ProdottoBaseService;
import it.unicam.cs.filieraagricola.api.services.elemento.ProdottoService;
import it.unicam.cs.filieraagricola.api.services.elemento.ProdottoTrasformatoService;
import it.unicam.cs.filieraagricola.api.services.gestore.CategoriaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.unicam.cs.filieraagricola.api.commons.ResponseEntityUtil.unauthorizedResponse;

@RestController
@RequestMapping("/operatore/trasformatore")
public class ProdottoTrasformatoController {

    @Autowired
    private ProdottoTrasformatoService prodottoTrasformatoService;
    @Autowired
    private ProdottoBaseService prodottoBaseService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoriaService categoriaService;

    @RequestMapping({"/prodottitrasformati"})
    public ResponseEntity<Object> getProdottiTrasformati() {
        if(!prodottoTrasformatoService.getProdottiTrasformati().isEmpty()){
            return new ResponseEntity<>(prodottoTrasformatoService.getProdottiTrasformati(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Nessun prodotto trasformato esistente",HttpStatus.NOT_FOUND);
    }

    @RequestMapping({"/prodottitrasformati/{id}"})
    public ResponseEntity<Object> getProdottoTrasformato(@PathVariable   int id) {
        if(prodottoTrasformatoService.getProdottoTrasformato(id) != null ){
            if(prodottoTrasformatoService.getProdottoTrasformato(id).getOperatore() == userService.getCurrentUser()) {
                return new ResponseEntity<>(prodottoTrasformatoService.getProdottoTrasformato(id), HttpStatus.FOUND);
            } else {
                return unauthorizedResponse();
            }
        }
        return new ResponseEntity<>("Prodotto trasformato non esistente", HttpStatus.NOT_FOUND);
    }

    @PostMapping({"/prodottitrasformati/aggiungi"})
    public ResponseEntity<Object> aggiungiProdottoTrasformato(@RequestBody ProdottoTrasformato prodotto) {
        ProdottoBase prodottoBase = prodottoBaseService.getProdottoBase(prodotto.getProdottoBase().getId());
        if (prodottoBase == null) {
            return new ResponseEntity<>("Prodotto base non trovato", HttpStatus.NOT_FOUND);
        }
        if (prodottoBase.getStatorichiesta() != StatoContenuto.ACCETTATA) {
            return new ResponseEntity<>("Il prodotto base associato deve essere stato precedentemente validato", HttpStatus.BAD_REQUEST);
        }

        if(prodottoTrasformatoService.aggiungiProdottoTrasformato(prodotto)){
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
        ProdottoBase prodottoBase = prodottoBaseService.getProdottoBase(IDprodottoBase);
        if (prodottoBase == null) {
            return new ResponseEntity<>("Prodotto base non trovato", HttpStatus.NOT_FOUND);
        }
        if (prodottoBase.getStatorichiesta() != StatoContenuto.ACCETTATA) {
            return new ResponseEntity<>("Il prodotto base associato deve essere stato precedentemente validato", HttpStatus.BAD_REQUEST);
        }
        if(!categoriaService.existsSameCategoria(categoria)){
            return new ResponseEntity<>("Categoria non esistente, Categorie esistenti: " + categoriaService.getAllCategorie(), HttpStatus.NOT_FOUND);
        }
        Categoria cat = categoriaService.getCategoriaByNome(categoria).get();
        if(prodottoTrasformatoService.aggiungiProdottoTrasformato(nome, processoTrasformazione, certificazioni, IDprodottoBase, descrizione, prezzo, cat)){
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto trasformato esistente", HttpStatus.CONFLICT);
    }

    @RequestMapping({"/prodottitrasformati/elimina"})
    public ResponseEntity<Object> eliminaProdottoTrasformato(@RequestParam int id) {
        if (prodottoTrasformatoService.getProdottoTrasformato(id) != null) {
            if (prodottoTrasformatoService.getProdottoTrasformato(id).getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            prodottoTrasformatoService.deleteProdottoTrasformato(id);
            return new ResponseEntity<>("Prodotto trasformato eliminato", HttpStatus.OK);
        } else return new ResponseEntity<>("Prodotto non esistente", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottitrasformati/aggiorna")
    public ResponseEntity<Object> aggiornaProdottoTrasformato(@RequestBody ProdottoTrasformato prodottoTrasformato) {
        if (prodottoTrasformatoService.existsProdottoTrasformato(prodottoTrasformato.getId())) {
            ProdottoTrasformato prodotto = prodottoTrasformatoService.getProdottoTrasformato(prodottoTrasformato.getId());
            if (prodotto.getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            if (prodottoTrasformatoService.aggiornaProdottoTrasformato(prodottoTrasformato)) {
                return new ResponseEntity<>("Prodotto trasformato aggiornato", HttpStatus.FOUND);
            }
            return new ResponseEntity<>("Prodotto non aggiornato", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Prodotto non esistente", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottitrasformati/aggiorna-quantita")
    public ResponseEntity<Object> aggiornaQuantitaProdotto(@RequestParam int id, @RequestParam int quantita) {
        ProdottoTrasformato prodotto = prodottoTrasformatoService.getProdottoTrasformato(id);
        if (prodotto != null) {
            if (prodotto.getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            if (prodottoTrasformatoService.aggiornaQuantitaProdotto(id, quantita)) {
                return new ResponseEntity<>("Quantità aggiornata", HttpStatus.OK);
            }
            return new ResponseEntity<>("Impossibile aggiornare la quantità", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Prodotto non trovato", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottitrasformati/rimuovi-quantita")
    public ResponseEntity<Object> rimuoviQuantitaProdotto(@RequestParam int id, @RequestParam int quantita) {
        ProdottoTrasformato prodotto = prodottoTrasformatoService.getProdottoTrasformato(id);
        if (prodotto != null) {
            if (prodotto.getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            if (prodottoTrasformatoService.riduciQuantitaProdotto(id, quantita)) {
                return new ResponseEntity<>("Quantità ridotta", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Quantità insufficiente per rimuovere", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Prodotto non trovato", HttpStatus.NOT_FOUND);
    }
}
