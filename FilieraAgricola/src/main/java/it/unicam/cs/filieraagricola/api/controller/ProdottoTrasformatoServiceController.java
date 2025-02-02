package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

<<<<<<< HEAD:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/services/ProdottoTrasformatoService.java
@Service
public class ProdottoTrasformatoService {
=======
@RestController
@RequestMapping("/trasformatore")
public class ProdottoTrasformatoServiceController {

>>>>>>> ba47a2edbe86bce5cfd774dfb1a050ef55a48362:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/controller/ProdottoTrasformatoServiceController.java
    @Autowired
    private final ProdottoRepository prodottoRepository;
    @Autowired
<<<<<<< HEAD:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/services/ProdottoTrasformatoService.java
    private ContenutoService contenutoService;

    public ProdottoTrasformatoService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
=======
    private final ProdottoBaseRepository prodottoBaseRepository;
    public ProdottoTrasformatoServiceController(ProdottoTrasformatoRepository prodottoTrasformatoRepository,
                                                ProdottoBaseRepository prodottoBaseRepository) {
        this.prodottoBaseRepository = prodottoBaseRepository;
        this.prodottoTrasformatoRepository = prodottoTrasformatoRepository;
>>>>>>> ba47a2edbe86bce5cfd774dfb1a050ef55a48362:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/controller/ProdottoTrasformatoServiceController.java
    }



<<<<<<< HEAD:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/services/ProdottoTrasformatoService.java
    public ResponseEntity<Object> getProdottoTrasformato(int id) {
        if (!this.prodottoRepository.existsProdottoTrasformatoById(id)) {
=======
    @RequestMapping({"/prodottitrasformati"})
    public ResponseEntity<Object> getProdotti() {
        return new ResponseEntity<>(this.prodottoTrasformatoRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping({"/prodottitrasformati/{id}"})
    public ResponseEntity<Object> getProdotto(@PathVariable("id") int id) {
        if (!this.prodottoTrasformatoRepository.existsById(id)) {
>>>>>>> ba47a2edbe86bce5cfd774dfb1a050ef55a48362:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/controller/ProdottoTrasformatoServiceController.java
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.prodottoRepository.findById(id), HttpStatus.OK);
        }
    }

<<<<<<< HEAD:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/services/ProdottoTrasformatoService.java
    public ResponseEntity<Object> getProdottiTrasformati() {
        return new ResponseEntity<>(this.prodottoRepository.findAllProdottiTrasformati(), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteProdottoTrasformato(int id) {
        this.prodottoRepository.deleteById(id);
        return new ResponseEntity<>("Product with id: " + id + " Deleted", HttpStatus.OK);
    }

    public ResponseEntity<Object> aggiornaProdottoTrasformato(ProdottoTrasformato prodottoTrasformato) {
        if (this.prodottoRepository.existsProdottoTrasformatoById(prodottoTrasformato.getId())) {
            this.prodottoRepository.save(prodottoTrasformato);
            return new ResponseEntity<>("Product " + prodottoTrasformato.getId() + " Updated", HttpStatus.OK);
=======
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
>>>>>>> ba47a2edbe86bce5cfd774dfb1a050ef55a48362:FilieraAgricola/src/main/java/it/unicam/cs/filieraagricola/api/controller/ProdottoTrasformatoServiceController.java
        } else {
            return ResponseEntity.status(404).body("Prodotto " + prodottoTrasformato.getId() + " Non Trovato");
        }
    }

    public ResponseEntity<Object> aggiungiProdottoTrasformato(String nome, String processo,
                                                            String certificazioni, int prodottoBaseID,
                                                              String descrizione, double prezzo) {

        Optional<ProdottoBase> prodottoBaseOpt = prodottoRepository.findProdottoBaseById(prodottoBaseID);
        ProdottoBase prodottoBase;
        if(prodottoBaseOpt.isPresent()) {
            prodottoBase = prodottoBaseOpt.get();
        } else {
            return new ResponseEntity<>("ProdottoBase non trovato!", HttpStatus.BAD_REQUEST);
        }
        if(prodottoRepository.existsByCaratteristicheTrasformato(nome, processo, certificazioni, prodottoBase)){
            return new ResponseEntity<>("Prodotto già esistente!", HttpStatus.CONFLICT);
        }
        creaTrasformato(nome, processo, certificazioni, prodottoBaseID, descrizione, prezzo);
        return new ResponseEntity<>("Prodotto Trasformato creato!", HttpStatus.OK);
    }

    public ResponseEntity<Object> aggiungiProdottoTrasformato(ProdottoTrasformato prodottoTrasformato) {
        if(prodottoRepository.existsById(prodottoTrasformato.getId())) {
            return new ResponseEntity<>("Prodotto già esistente!", HttpStatus.CONFLICT);
        }
        contenutoService.aggiungiContenutoDaElemento(prodottoTrasformato);
        prodottoRepository.save(prodottoTrasformato);
        return new ResponseEntity<>("Prodotto Trasformato creato!", HttpStatus.OK);
    }


    public void creaTrasformato(String nome, String processo, String certificazioni, int prodottoBaseID,
                                String descrizione, double prezzo) {
        ProdottoTrasformato prodottoTrasformato = new ProdottoTrasformato();
        ProdottoBase prodottoBase = prodottoRepository.findProdottoBaseById(prodottoBaseID).get(); //Il controllo per esistenza viene già effettuato in esisteProdottoTrasformato
        prodottoTrasformato.setNome(nome);
        prodottoTrasformato.setProcessoTrasformazione(processo);
        prodottoTrasformato.setCertificazioni(certificazioni);
        prodottoTrasformato.setProdottoBase(prodottoBase);
        prodottoTrasformato.setDescrizione(descrizione);
        prodottoTrasformato.setPrezzo(prezzo);
        prodottoRepository.save(prodottoTrasformato);
        contenutoService.aggiungiContenutoDaElemento(prodottoTrasformato);
    }
}
