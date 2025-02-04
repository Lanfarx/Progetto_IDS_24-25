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
        if(!prodottoBaseService.getProdottiBase().isEmpty()){
            return new ResponseEntity<>(prodottoBaseService.getProdottiBase(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Non esistono prodotti base", HttpStatus.NOT_FOUND);
    }

    @RequestMapping({"/prodottibase/{id}"})
    public ResponseEntity<Object> getProdottoBase(@PathVariable("id") int id) {
        if(prodottoBaseService.getProdottoBase(id) != null){
            return new ResponseEntity<>(prodottoBaseService.getProdottoBase(id), HttpStatus.FOUND);
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
                                                       @RequestParam("prezzo") double prezzo) {
        if(prodottoBaseService.aggiungiProdottoBase(nome, metodiDiColtivazione, certificazioni, descrizione, prezzo)){
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Prodotto già esistente", HttpStatus.CONFLICT);
    }

    @DeleteMapping({"/prodottibase/elimina/{id}"})
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") int id) {
        prodottoBaseService.deleteProdottoBase(id);
        return new ResponseEntity<>("Prodotto eliminato", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/prodottibase/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> aggiornaProdottoBase(@RequestBody ProdottoBase prodottoBase) {
        if(prodottoBaseService.aggiornaProdottoBase(prodottoBase)){
            return new ResponseEntity<>("Prodotto aggiornato", HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Prodotto non esistente", HttpStatus.NOT_FOUND);
    }
}
