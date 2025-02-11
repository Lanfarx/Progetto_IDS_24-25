package it.unicam.cs.filieraagricola.api.controller.elemento;
import it.unicam.cs.filieraagricola.api.entities.elemento.Categoria;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.elemento.ProdottoBaseService;
import it.unicam.cs.filieraagricola.api.services.gestore.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.unicam.cs.filieraagricola.api.commons.utils.ResponseEntityUtil.unauthorizedResponse;

@RestController
@RequestMapping("/operatore/produttore")
public class ProdottoBaseController {

    @Autowired
    private ProdottoBaseService prodottoBaseService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoriaService categoriaService;

    @RequestMapping({"/prodottibase"})
    public ResponseEntity<Object> getProdottiBase() {
        if(!prodottoBaseService.getProdottiBase().isEmpty()){
            return new ResponseEntity<>(prodottoBaseService.getProdottiBase(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Non esistono prodotti base", HttpStatus.NOT_FOUND);
    }

    @RequestMapping({"/prodottibase/{id}"})
    public ResponseEntity<Object> getProdottoBase(@PathVariable int id) {
        if(prodottoBaseService.getProdottoBase(id) != null){
            if(prodottoBaseService.getProdottoBase(id).getOperatore() == userService.getCurrentUser()) {
                return new ResponseEntity<>(prodottoBaseService.getProdottoBase(id), HttpStatus.FOUND);
            } else return unauthorizedResponse();
        }
        return new ResponseEntity<>("Prodotto base non esistente", HttpStatus.NOT_FOUND);
    }

    @PostMapping({"/prodottibase/aggiungi"})
    public ResponseEntity<Object> aggiungiProdottoBase(@RequestBody ProdottoBase prodotto) {
        if(!categoriaService.existsSameCategoria(prodotto.getCategoria().getNome())){
            return new ResponseEntity<>("Categoria non esistente, Categorie esistenti: " + categoriaService.getAllCategorie(), HttpStatus.NOT_FOUND);
        }
        if(prodottoBaseService.aggiungiProdottoBase(prodotto)){
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

        if(!categoriaService.existsSameCategoria(categoria)){
            return new ResponseEntity<>("Categoria non esistente, Categorie esistenti: " + categoriaService.getAllCategorie(), HttpStatus.NOT_FOUND);
        }
        Categoria cat = categoriaService.getCategoriaByNome(categoria).get();
        if(prodottoBaseService.aggiungiProdottoBase(nome, metodiDiColtivazione, certificazioni, descrizione, prezzo, cat)){
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto già esistente", HttpStatus.CONFLICT);
    }

    @DeleteMapping({"/prodottibase/elimina"})
    public ResponseEntity<Object> eliminaProdotto(@RequestParam int id) {
        if(prodottoBaseService.getProdottoBase(id) != null) {
            if (prodottoBaseService.getProdottoBase(id).getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            prodottoBaseService.deleteProdottoBase(id);
            return new ResponseEntity<>("Prodotto eliminato", HttpStatus.OK);
        } else return new ResponseEntity<>("Prodotto non esistente", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottibase/aggiorna")
    public ResponseEntity<Object> aggiornaProdottoBase(@RequestBody ProdottoBase prodottoBase) {
        if(prodottoBaseService.existsProdottoBase(prodottoBase.getId())){
            ProdottoBase prodotto = prodottoBaseService.getProdottoBase(prodottoBase.getId());
            if (prodotto.getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            if (prodottoBaseService.aggiornaProdottoBase(prodottoBase)) {
                return new ResponseEntity<>("Prodotto aggiornato", HttpStatus.FOUND);
            }
            return new ResponseEntity<>("Prodotto non aggiornato", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Prodotto non esistente", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottibase/aggiorna-quantita")
    public ResponseEntity<Object> aggiornaQuantitaProdotto(@RequestParam int id, @RequestParam int quantita) {
        ProdottoBase prodotto = prodottoBaseService.getProdottoBase(id);
        if (prodotto != null) {
            if (prodotto.getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            if (prodottoBaseService.aggiornaQuantitaProdotto(id, quantita)) {
                return new ResponseEntity<>("Quantità aggiornata", HttpStatus.OK);
            }
            return new ResponseEntity<>("Impossibile aggiornare la quantità", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Prodotto non trovato", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/prodottibase/rimuovi-quantita")
    public ResponseEntity<Object> rimuoviQuantitaProdotto(@RequestParam int id, @RequestParam int quantita) {
        ProdottoBase prodotto = prodottoBaseService.getProdottoBase(id);
        if (prodotto != null) {
            if (prodotto.getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            if (prodottoBaseService.riduciQuantitaProdotto(id, quantita)) {
                return new ResponseEntity<>("Quantità ridotta", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Quantità insufficiente per rimuovere", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Prodotto non trovato", HttpStatus.NOT_FOUND);
    }
}
