package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.repository.ProdottoBaseRepository;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProdottoBaseService {
    @Autowired
    private ProdottoBaseRepository prodottoBaseRepository;

    public ProdottoBaseService(ProdottoBaseRepository prodottoBaseRepository) {
        this.prodottoBaseRepository = prodottoBaseRepository;
    }

    @PostConstruct
    public void initSampleData() {
        ProdottoBase miele = new ProdottoBase();
        miele.setId(1);
        miele.setNome("Miele");
        miele.setCertificazioni("Allevato in Italia");
        miele.setMetodiDiColtivazione("Raccolto senza maltrattare api");
        miele.setPrezzo(10.0);
        prodottoBaseRepository.save(miele);

        ProdottoBase pomodoro = new ProdottoBase();
        pomodoro.setId(2);
        pomodoro.setNome("Pomodoro");
        pomodoro.setCertificazioni("Coltivato in Italia");
        pomodoro.setMetodiDiColtivazione("Coltivato usando acqua depurata");
        pomodoro.setPrezzo(5.0);
        prodottoBaseRepository.save(pomodoro);
    }

    @RequestMapping({"/prodottibase"})
    public ResponseEntity<Object> getProducts() {
        return new ResponseEntity<>(this.prodottoBaseRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping({"/prodottibase/mostra/{id}"})
    public ResponseEntity<Object> getProduct(@PathVariable("id") int id) {
        if (!this.prodottoBaseRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.prodottoBaseRepository.findById(id), HttpStatus.OK);
        }
    }

    @PostMapping({"/prodottibase/aggiungi"})
    public ResponseEntity<Object> addProduct(@RequestBody ProdottoBase prodotto) {
        if (!this.prodottoBaseRepository.existsById(prodotto.getId())) {
            this.prodottoBaseRepository.save(prodotto);
            return new ResponseEntity<>("Product Created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Product Already Exists", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping({"prodottibase/aggiungiconparametri"})
    public ResponseEntity<Object> addProductWithParam(@PathParam("id") int id,
                                                      @PathParam("nome") String nome,
                                                      @PathParam("prezzo") double prezzo,
                                                      @PathParam("metodiDiColtivazione") String metodiDiColtivazione,
                                                      @PathParam("certificazioni") String certificazioni) {
        if (!this.prodottoBaseRepository.existsById(id)) {
            ProdottoBase prodotto = new ProdottoBase();
            prodotto.setId(id);
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

    @RequestMapping({"/prodottibase/elimina/{id}"})
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") int id) {
        this.prodottoBaseRepository.deleteById(id);
        return new ResponseEntity<>("Product " + id + " Deleted", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/prodottibase/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> updateProduct(@RequestBody ProdottoBase prodottoBase) {
        if (this.prodottoBaseRepository.existsById(prodottoBase.getId())) {
            this.prodottoBaseRepository.save(prodottoBase);
            return new ResponseEntity<>("Product " + prodottoBase.getId() + " Updated", HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("Prodotto " + prodottoBase.getId() + " Not Found");
        }
    }
}
