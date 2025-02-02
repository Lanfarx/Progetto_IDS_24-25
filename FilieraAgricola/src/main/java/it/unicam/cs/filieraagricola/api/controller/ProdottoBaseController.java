package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.services.PacchettoService;
import it.unicam.cs.filieraagricola.api.services.ProdottoBaseService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produttore")
public class ProdottoBaseController {
    @Autowired
    private ProdottoBaseService prodottoBaseService;


  /*  @PostConstruct
    public void initSampleData() {
        ProdottoBase miele = new ProdottoBase();
        miele.setNome("Miele");
        miele.setCertificazioni("Allevato in Italia");
        miele.setMetodiDiColtivazione("Raccolto senza maltrattare api");
        miele.setPrezzo(10.0);
        prodottoBaseRepository.save(miele);

        ProdottoBase pomodoro = new ProdottoBase();
        pomodoro.setNome("Pomodoro");
        pomodoro.setCertificazioni("Coltivato in Italia");
        pomodoro.setMetodiDiColtivazione("Coltivato usando acqua depurata");
        pomodoro.setPrezzo(5.0);
        prodottoBaseRepository.save(pomodoro);
    }*/

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Benvenuto nella dashboard del Produttore");
    }

    @RequestMapping({"/prodottibase"})
    public ResponseEntity<Object> getProdottiBase() {
        return prodottoBaseService.getProdottiBase();
    }

    @RequestMapping({"/prodottibase/{id}"})
    public ResponseEntity<Object> getProdottoBase(@PathVariable("id") int id) {
        return prodottoBaseService.getProdottoBase(id);
    }

    @PostMapping({"/prodottibase/aggiungi"})
    public ResponseEntity<Object> aggiungiProdottoBase(@RequestBody ProdottoBase prodotto) {
        return prodottoBaseService.aggiungiProdottoBase(prodotto);
    }

    @PostMapping({"prodottibase/aggiungiconparametri"})
    public ResponseEntity<Object> aggiungiProdottoBase(@RequestParam("nome") String nome,
                                                      @RequestParam("metodiDiColtivazione") String metodiDiColtivazione,
                                                      @RequestParam("certificazioni") String certificazioni,
                                                       @RequestParam("descrizione") String descrizione,
                                                       @RequestParam("prezzo") double prezzo) {
        return prodottoBaseService.aggiungiProdottoBase(nome, metodiDiColtivazione, certificazioni, descrizione, prezzo);
    }

    @DeleteMapping({"/prodottibase/elimina/{id}"})
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") int id) {
        return  prodottoBaseService.deleteProdottoBase(id);
    }

    @RequestMapping(
            value = {"/prodottibase/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> aggiornaProdottoBase(@RequestBody ProdottoBase prodottoBase) {
        return prodottoBaseService.aggiungiProdottoBase(prodottoBase);
    }
}
