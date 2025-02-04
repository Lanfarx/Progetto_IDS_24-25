package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.services.ProdottoTrasformatoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trasformatore")
public class ProdottoTrasformatoController {

    @Autowired
    private final ProdottoTrasformatoService prodottoTrasformatoService;
    public ProdottoTrasformatoController(ProdottoTrasformatoService prodottoTrasformatoService) {
        this.prodottoTrasformatoService = prodottoTrasformatoService;
    }

    @PostConstruct
    public void initSampleData() {
/*        //Prodotto trasformato derivante da quello sopra
        ProdottoBase prodottoBase = new ProdottoBase();
        prodottoBase.setNome("Pummador");
        prodottoBase.setCertificazioni("AA");
        prodottoBase.setMetodiDiColtivazione("dsa");
        prodottoRepository.save(prodottoBase);
        ProdottoTrasformato passataPomodoro = new ProdottoTrasformato();
        passataPomodoro.setNome("Passata Pomodoro");
        passataPomodoro.setCertificazioni("DOP");
        passataPomodoro.setProdottoBase(prodottoBase);
        passataPomodoro.setProcessoTrasformazione("Schiacciato da pressa");
        prodottoRepository.save(passataPomodoro);
        System.out.println("Prodotto trasformato creato: " + passataPomodoro);*/
    }

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
            return new ResponseEntity<>(prodottoTrasformatoService.getProdottoTrasformato(id), HttpStatus.FOUND);
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
                                                      @RequestParam("prezzo") double prezzo)
    {
        if(prodottoTrasformatoService.aggiungiProdottoTrasformato(nome, processoTrasformazione, certificazioni, IDprodottoBase, descrizione, prezzo)){
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto trasformato esistente", HttpStatus.CONFLICT);
    }

    @RequestMapping({"/prodottitrasformati/elimina/{id}"})
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") int id) {
        prodottoTrasformatoService.deleteProdottoTrasformato(id);
        return new ResponseEntity<>("Prodotto trasformato eliminato", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/prodottitrasformati/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> updateProduct(@RequestBody ProdottoTrasformato prodottoTrasformato) {
        if(prodottoTrasformatoService.aggiornaProdottoTrasformato(prodottoTrasformato)){
            return new ResponseEntity<>("Prodotto trasformato aggiornato", HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Prodotto non trovato", HttpStatus.NOT_FOUND);
    }
}
