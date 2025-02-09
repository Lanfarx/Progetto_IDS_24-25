package it.unicam.cs.filieraagricola.api.controller.elemento;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.Categoria;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.elemento.ProdottoBaseService;
import it.unicam.cs.filieraagricola.api.services.gestore.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operatore/produttore")
public class ProdottoBaseController {
    @Autowired
    private ProdottoBaseService prodottoBaseService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Benvenuto nella dashboard del Produttore");
    }

    @RequestMapping({"/prodottibase"})
    public ResponseEntity<Object> getProdottiBase() {
        if(!prodottoBaseService.getProdottiBase().isEmpty()){
            return new ResponseEntity<>(prodottoBaseService.getProdottiBase(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Non esistono prodotti base", HttpStatus.NOT_FOUND);
    }

    @RequestMapping({"/prodottibase/{id}"})
    public ResponseEntity<Object> getProdottoBase(@PathVariable("id") int id) {
        if(prodottoBaseService.getProdottoBase(id) != null){
            if(prodottoBaseService.getProdottoBase(id).getOperatore() == userService.getCurrentUser()) {
                return new ResponseEntity<>(prodottoBaseService.getProdottoBase(id), HttpStatus.FOUND);
            } else {
                return new ResponseEntity<>("Non sei autorizzato a vedere questo Prodotto", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("Prodotto base non esistente", HttpStatus.NOT_FOUND);
    }

    @PostMapping({"/prodottibase/aggiungi"})
    public ResponseEntity<Object> aggiungiProdottoBase(@RequestBody ProdottoBase prodotto) {
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

    @DeleteMapping({"/prodottibase/elimina/{id}"})
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") int id) {
        if(prodottoBaseService.getProdottoBase(id).getOperatore() != userService.getCurrentUser()) {
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.UNAUTHORIZED);
        }
        prodottoBaseService.deleteProdottoBase(id);
        return new ResponseEntity<>("Prodotto eliminato", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/prodottibase/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> aggiornaProdottoBase(@RequestBody ProdottoBase prodottoBase) {
        if(prodottoBase.getOperatore() != userService.getCurrentUser()) {
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.UNAUTHORIZED);
        }
        if(prodottoBaseService.aggiornaProdottoBase(prodottoBase)){
            return new ResponseEntity<>("Prodotto aggiornato", HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Prodotto non esistente", HttpStatus.NOT_FOUND);
    }
}
