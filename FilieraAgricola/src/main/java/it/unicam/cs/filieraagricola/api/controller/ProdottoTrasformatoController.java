package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
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
        return prodottoTrasformatoService.getProdottiTrasformati();
    }

    @RequestMapping({"/prodottitrasformati/{id}"})
    public ResponseEntity<Object> getProduct(@PathVariable("id") int id) {
        return prodottoTrasformatoService.getProdottoTrasformato(id);
    }

    @PostMapping({"/prodottitrasformati/aggiungi"})
    public ResponseEntity<Object> addProduct(@RequestBody ProdottoTrasformato prodotto) {
        return prodottoTrasformatoService.aggiungiProdottoTrasformato(prodotto);
    }

    @PostMapping({"prodottitrasformati/aggiungiconparametri"})
    public ResponseEntity<Object> addProductWithParam(@RequestParam("nome") String nome,
                                                      @RequestParam("processoTrasformazione") String processoTrasformazione,
                                                      @RequestParam("certificazioni") String certificazioni,
                                                      @RequestParam("prodottoBase") int IDprodottoBase,
                                                      @RequestParam("descrizione") String descrizione,
                                                      @RequestParam("prezzo") double prezzo)
    {
        return prodottoTrasformatoService.aggiungiProdottoTrasformato(nome, processoTrasformazione,
                                                                    certificazioni, IDprodottoBase,
                                                                    descrizione, prezzo);
        //TODO creaContenuto()
        //TODO CREARE CONTENUTO IN MANIERA AUTOMATICA AGGIUNGENDO PARAM PREZZO
        //TODO AGGIUNGERE UNA SERVICE O UNA REPOSITORY PER CONTENUTO
    }

    @RequestMapping({"/prodottitrasformati/elimina/{id}"})
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") int id) {
        return prodottoTrasformatoService.deleteProdottoTrasformato(id);
    }

    @RequestMapping(
            value = {"/prodottitrasformati/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> updateProduct(@RequestBody ProdottoTrasformato prodottoTrasformato) {
        return prodottoTrasformatoService.aggiornaProdottoTrasformato(prodottoTrasformato);
    }
}
