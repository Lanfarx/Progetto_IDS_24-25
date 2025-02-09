package it.unicam.cs.filieraagricola.api.controller.elemento;

import it.unicam.cs.filieraagricola.api.entities.elemento.Categoria;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.elemento.ProdottoTrasformatoService;
import it.unicam.cs.filieraagricola.api.services.gestore.CategoriaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operatore/trasformatore")
public class ProdottoTrasformatoController {

    @Autowired
    private ProdottoTrasformatoService prodottoTrasformatoService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Benvenuto nella dashboard del Trasformatore");
    }

    @RequestMapping({"/prodottitrasformati"})
    public ResponseEntity<Object> getProducts() {
        if(!prodottoTrasformatoService.getProdottiTrasformati().isEmpty()){
            return new ResponseEntity<>(prodottoTrasformatoService.getProdottiTrasformati(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Nessun prodotto trasformato esistente",HttpStatus.NOT_FOUND);
    }

    @RequestMapping({"/prodottitrasformati/{id}"})
    public ResponseEntity<Object> getProduct(@PathVariable("id") int id) {
        if(prodottoTrasformatoService.getProdottoTrasformato(id) != null ){
            if(prodottoTrasformatoService.getProdottoTrasformato(id).getOperatore() == userService.getCurrentUser()) {
                return new ResponseEntity<>(prodottoTrasformatoService.getProdottoTrasformato(id), HttpStatus.FOUND);
            } else {
                return new ResponseEntity<>("Non sei autorizzato a vedere questo prodotto", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("Prodotto trasformato non esistente", HttpStatus.NOT_FOUND);
    }

    @PostMapping({"/prodottitrasformati/aggiungi"})
    public ResponseEntity<Object> addProduct(@RequestBody ProdottoTrasformato prodotto) {
        if(prodottoTrasformatoService.aggiungiProdottoTrasformato(prodotto)){
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto trasformato esistente", HttpStatus.CONFLICT);
    }

    @PostMapping({"prodottitrasformati/aggiungiconparametri"})
    public ResponseEntity<Object> addProductWithParam(@RequestParam("nome") String nome,
                                                      @RequestParam("processoTrasformazione") String processoTrasformazione,
                                                      @RequestParam("certificazioni") String certificazioni,
                                                      @RequestParam("prodottoBase") int IDprodottoBase,
                                                      @RequestParam("descrizione") String descrizione,
                                                      @RequestParam("prezzo") double prezzo,
                                                      @RequestParam("categoria") String categoria)
    {
        if(!categoriaService.existsSameCategoria(categoria)){
            return new ResponseEntity<>("Categoria non esistente, Categorie esistenti: " + categoriaService.getAllCategorie(), HttpStatus.NOT_FOUND);
        }
        Categoria cat = categoriaService.getCategoriaByNome(categoria).get();
        if(prodottoTrasformatoService.aggiungiProdottoTrasformato(nome, processoTrasformazione, certificazioni, IDprodottoBase, descrizione, prezzo, cat)){
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto trasformato esistente", HttpStatus.CONFLICT);
    }

    @RequestMapping({"/prodottitrasformati/elimina/{id}"})
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") int id) {
        if(prodottoTrasformatoService.getProdottoTrasformato(id).getOperatore() != userService.getCurrentUser()) {
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.UNAUTHORIZED);
        }
        prodottoTrasformatoService.deleteProdottoTrasformato(id);
        return new ResponseEntity<>("Prodotto trasformato eliminato", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/prodottitrasformati/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> updateProduct(@RequestBody ProdottoTrasformato prodottoTrasformato) {
        if(prodottoTrasformato.getOperatore() != userService.getCurrentUser()) {
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.UNAUTHORIZED);
        }
        if(prodottoTrasformatoService.aggiornaProdottoTrasformato(prodottoTrasformato)){
            return new ResponseEntity<>("Prodotto trasformato aggiornato", HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Prodotto non trovato", HttpStatus.NOT_FOUND);
    }
}
