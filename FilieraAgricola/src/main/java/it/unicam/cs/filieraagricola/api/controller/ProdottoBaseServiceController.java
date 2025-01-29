package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.repository.ProdottoBaseRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produttore")
public class ProdottoBaseServiceController {
    @Autowired
    private ProdottoBaseRepository prodottoBaseRepository;

    public ProdottoBaseServiceController(ProdottoBaseRepository prodottoBaseRepository) {
        this.prodottoBaseRepository = prodottoBaseRepository;
    }

    @PostConstruct
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
    }

    @RequestMapping({"/prodottibase"})
    public ResponseEntity<Object> getProdotti() {
        return new ResponseEntity<>(this.prodottoBaseRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping({"/prodottibase/{id}"})
    public ResponseEntity<Object> getProdotto(@PathVariable("id") int id) {
        if (!this.prodottoBaseRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.prodottoBaseRepository.findById(id), HttpStatus.OK);
        }
    }

    @PostMapping({"/prodottibase/aggiungi"})
    public ResponseEntity<Object> aggiungiProdotto(@RequestBody ProdottoBase prodotto) {
        if (!this.prodottoBaseRepository.existsByNomeAndCertificazioniAndMetodiDiColtivazioneAndPrezzo(
                prodotto.getNome(), prodotto.getCertificazioni(),
                prodotto.getMetodiDiColtivazione(), prodotto.getPrezzo())) {
            this.prodottoBaseRepository.save(prodotto);
            return new ResponseEntity<>("Prodotto Creato", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Prodotto già esistente", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping({"prodottibase/aggiungiconparametri"})
    public ResponseEntity<Object> aggiungiProdottoConParametri(@RequestParam("nome") String nome,
                                                      @RequestParam("prezzo") double prezzo,
                                                      @RequestParam("metodiDiColtivazione") String metodiDiColtivazione,
                                                      @RequestParam("certificazioni") String certificazioni) {
        if (!this.prodottoBaseRepository.existsByNomeAndCertificazioniAndMetodiDiColtivazioneAndPrezzo(
                nome, certificazioni, metodiDiColtivazione, prezzo)) {
            ProdottoBase prodotto = new ProdottoBase();
            prodotto.setNome(nome);
            prodotto.setMetodiDiColtivazione(metodiDiColtivazione);
            prodotto.setCertificazioni(certificazioni);
            prodotto.setPrezzo(prezzo);
            this.prodottoBaseRepository.save(prodotto);
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Il prodotto già esiste", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping({"/prodottibase/elimina/{id}"})
    public ResponseEntity<Object> eiminaProdotto(@PathVariable("id") int id) {
        this.prodottoBaseRepository.deleteById(id);
        return new ResponseEntity<>("Product " + id + " Deleted", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/prodottibase/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> aggiornaProdotto(@RequestBody ProdottoBase prodottoBase) {
        if (this.prodottoBaseRepository.existsById(prodottoBase.getId())) {
            this.prodottoBaseRepository.save(prodottoBase);
            return new ResponseEntity<>("Prodotto " + prodottoBase.getId() + " Aggiornato", HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("Prodotto " + prodottoBase.getId() + " Non Trovato");
        }
    }
}
