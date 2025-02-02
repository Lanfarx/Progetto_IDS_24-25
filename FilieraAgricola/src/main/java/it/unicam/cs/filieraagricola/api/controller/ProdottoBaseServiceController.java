package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

<<<<<<< HEAD:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/services/ProdottoBaseService.java
import java.util.Optional;

@Service
public class ProdottoBaseService {
=======
@RestController
@RequestMapping("/produttore")
public class ProdottoBaseServiceController {
>>>>>>> ba47a2edbe86bce5cfd774dfb1a050ef55a48362:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/controller/ProdottoBaseServiceController.java
    @Autowired
    private final ProdottoRepository prodottoRepository;
    @Autowired
    private ContenutoService contenutoService;

<<<<<<< HEAD:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/services/ProdottoBaseService.java
    public ProdottoBaseService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
=======
    public ProdottoBaseServiceController(ProdottoBaseRepository prodottoBaseRepository) {
        this.prodottoBaseRepository = prodottoBaseRepository;
>>>>>>> ba47a2edbe86bce5cfd774dfb1a050ef55a48362:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/controller/ProdottoBaseServiceController.java
    }


    public ResponseEntity<Object> aggiungiProdottoBase(String nome, String metodiColtivazione,
                                                              String certificazioni, String descrizione, double prezzo) {
        if(prodottoRepository.existsByCaratteristicheBase(nome, metodiColtivazione, certificazioni)){
            return new ResponseEntity<>("Prodotto già esistente!", HttpStatus.CONFLICT);
        }
        creaBase(nome, metodiColtivazione, certificazioni, descrizione, prezzo);
        return new ResponseEntity<>("Prodotto Base creato!", HttpStatus.OK);
    }

<<<<<<< HEAD:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/services/ProdottoBaseService.java
    public ResponseEntity<Object> aggiungiProdottoBase(ProdottoBase prodottoBase) {
        if(prodottoRepository.existsById(prodottoBase.getId()) || contenutoService.getContenutoByParam(prodottoBase) != null) {
            return new ResponseEntity<>("Prodotto già esistente!", HttpStatus.CONFLICT);
        }
        prodottoRepository.save(prodottoBase);
        contenutoService.aggiungiContenutoDaElemento(prodottoBase);
        return new ResponseEntity<>("Prodotto Base creato!", HttpStatus.OK);
    }

    public ResponseEntity<Object> getProdottoBase(int id) {
        if (!this.prodottoRepository.existsProdottoBaseById(id)) {
=======
    @RequestMapping({"/prodottibase"})
    public ResponseEntity<Object> getProdotti() {
        return new ResponseEntity<>(this.prodottoBaseRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping({"/prodottibase/{id}"})
    public ResponseEntity<Object> getProdotto(@PathVariable("id") int id) {
        if (!this.prodottoBaseRepository.existsById(id)) {
>>>>>>> ba47a2edbe86bce5cfd774dfb1a050ef55a48362:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/controller/ProdottoBaseServiceController.java
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.prodottoRepository.findById(id), HttpStatus.OK);
        }
    }

<<<<<<< HEAD:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/services/ProdottoBaseService.java
    public ResponseEntity<Object> getProdottiBase() {
        return new ResponseEntity<>(this.prodottoRepository.findAllProdottiBase(), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteProdottoBase(int id) {
        this.prodottoRepository.deleteById(id);
        return new ResponseEntity<>("Product with id: " + id + " Deleted", HttpStatus.OK);
    }

    public ResponseEntity<Object> aggiornaProdottoBase(ProdottoBase prodottoBase) {
        if (this.prodottoRepository.existsProdottoBaseById(prodottoBase.getId())) {
            this.prodottoRepository.save(prodottoBase);
            return new ResponseEntity<>("Product " + prodottoBase.getId() + " Updated", HttpStatus.OK);
=======
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
>>>>>>> ba47a2edbe86bce5cfd774dfb1a050ef55a48362:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/controller/ProdottoBaseServiceController.java
        } else {
            return ResponseEntity.status(404).body("Prodotto " + prodottoBase.getId() + " Non Trovato");
        }
    }

    public void creaBase(String nome, String metodiColtivazione, String certificazioni, String descrizone, double prezzo) {
        ProdottoBase prodottoBase = new ProdottoBase();
        prodottoBase.setNome(nome);
        prodottoBase.setMetodiDiColtivazione(metodiColtivazione);
        prodottoBase.setCertificazioni(certificazioni);
        prodottoBase.setDescrizione(descrizone);
        prodottoBase.setPrezzo(prezzo);
        contenutoService.aggiungiContenutoDaElemento(prodottoBase);
        prodottoRepository.save(prodottoBase);
    }
}
