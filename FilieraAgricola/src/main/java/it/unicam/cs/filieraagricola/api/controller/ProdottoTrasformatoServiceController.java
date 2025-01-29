package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.repository.ProdottoBaseRepository;
import it.unicam.cs.filieraagricola.api.repository.ProdottoTrasformatoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/trasformatore")
public class ProdottoTrasformatoServiceController {

    @Autowired
    private final ProdottoTrasformatoRepository prodottoTrasformatoRepository;
    @Autowired
    private final ProdottoBaseRepository prodottoBaseRepository;
    public ProdottoTrasformatoServiceController(ProdottoTrasformatoRepository prodottoTrasformatoRepository,
                                                ProdottoBaseRepository prodottoBaseRepository) {
        this.prodottoBaseRepository = prodottoBaseRepository;
        this.prodottoTrasformatoRepository = prodottoTrasformatoRepository;
    }

    @PostConstruct
    public void initSampleData() {
        //Prodotto trasformato derivante da quello sopra
        ProdottoBase prodottoBase = new ProdottoBase();
        prodottoBase.setNome("Pummador");
        prodottoBase.setPrezzo(30);
        prodottoBase.setCertificazioni("AA");
        prodottoBase.setMetodiDiColtivazione("dsa");
        prodottoBaseRepository.save(prodottoBase);
        ProdottoTrasformato passataPomodoro = new ProdottoTrasformato();
        passataPomodoro.setNome("Passata Pomodoro");
        passataPomodoro.setCertificazioni("DOP");
        passataPomodoro.setPrezzo(30);
        ProdottoBase pomodoro = prodottoBaseRepository.getReferenceById(3);
        passataPomodoro.setProdottoBase(pomodoro);
        passataPomodoro.setProcessoTrasformazione("Schiacciato da pressa");
        prodottoTrasformatoRepository.save(passataPomodoro);
        System.out.println("Prodotto trasformato creato: " + passataPomodoro);

    }

    @RequestMapping({"/prodottitrasformati"})
    public ResponseEntity<Object> getProdotti() {
        return new ResponseEntity<>(this.prodottoTrasformatoRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping({"/prodottitrasformati/{id}"})
    public ResponseEntity<Object> getProdotto(@PathVariable("id") int id) {
        if (!this.prodottoTrasformatoRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.prodottoTrasformatoRepository.findById(id), HttpStatus.OK);
        }
    }

    @PostMapping({"/prodottitrasformati/aggiungi"})
    public ResponseEntity<Object> aggiungiProdotto(@RequestBody ProdottoTrasformato prodotto) {
        if (!this.prodottoTrasformatoRepository.existsById(prodotto.getId())) {
            this.prodottoTrasformatoRepository.save(prodotto);
            return new ResponseEntity<>("Prodotto Creato", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Prodotto già esistente", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping({"prodottitrasformati/aggiungiconparametri"})
    public ResponseEntity<Object> aggiungiProdottoConParametri(@RequestParam("nome") String nome,
                                                      @RequestParam("prezzo") double prezzo,
                                                      @RequestParam("certificazioni") String certificazioni,
                                                      @RequestParam("prodottoBase") int IDprodottoBase,
                                                      @RequestParam("processoTrasformazione") String processoTrasformazione)
    {
        Optional<ProdottoBase> prodottoBaseOptional = this.prodottoBaseRepository.findById(IDprodottoBase);
        ProdottoBase prodottoBase = prodottoBaseOptional.get();
        if (!this.prodottoTrasformatoRepository.existsByNomeAndProcessoTrasformazioneAndCertificazioniAndPrezzoAndProdottoBase(
                nome, processoTrasformazione, certificazioni, prezzo, prodottoBase)) {
            ProdottoTrasformato prodotto = new ProdottoTrasformato();
            prodotto.setNome(nome);
            prodotto.setCertificazioni(certificazioni);
            prodotto.setPrezzo(prezzo);
            prodotto.setProcessoTrasformazione(processoTrasformazione);
            prodotto.setProdottoBase(prodottoBase);
            this.prodottoTrasformatoRepository.save(prodotto);
            return new ResponseEntity<>("Prodotto creato", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Il prodotto già esiste", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping({"/prodottitrasformati/elimina/{id}"})
    public ResponseEntity<Object> eliminaProdotto(@PathVariable("id") int id) {
        this.prodottoTrasformatoRepository.deleteById(id);
        return new ResponseEntity<>("Product " + id + " Deleted", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/prodottitrasformati/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> aggiornaProdotto(@RequestBody ProdottoTrasformato prodottoTrasformato) {
        if (this.prodottoTrasformatoRepository.existsById(prodottoTrasformato.getId())) {
            this.prodottoTrasformatoRepository.save(prodottoTrasformato);
            return new ResponseEntity<>("Prodotto " + prodottoTrasformato.getId() + " Aggiornato", HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("Prodotto " + prodottoTrasformato.getId() + " Non Trovato");
        }
    }
}
