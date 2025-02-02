package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdottoBaseService {
    @Autowired
    private final ProdottoRepository prodottoRepository;
    @Autowired
    private ContenutoService contenutoService;

    public ProdottoBaseService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }


    public ResponseEntity<Object> aggiungiProdottoBase(String nome, String metodiColtivazione,
                                                       String certificazioni, String descrizione, double prezzo) {
        if(prodottoRepository.existsByCaratteristicheBase(nome, metodiColtivazione, certificazioni)){
            return new ResponseEntity<>("Prodotto già esistente!", HttpStatus.CONFLICT);
        }
        creaBase(nome, metodiColtivazione, certificazioni, descrizione, prezzo);
        return new ResponseEntity<>("Prodotto Base creato!", HttpStatus.OK);
    }

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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.prodottoRepository.findById(id), HttpStatus.OK);
        }
    }

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
        } else {
            return ResponseEntity.status(404).body("Prodotto " + prodottoBase.getId() + " Not Found");
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