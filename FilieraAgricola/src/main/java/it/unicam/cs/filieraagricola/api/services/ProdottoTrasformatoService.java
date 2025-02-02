package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProdottoTrasformatoService {
    @Autowired
    private final ProdottoRepository prodottoRepository;
    @Autowired
    private ContenutoService contenutoService;

    public ProdottoTrasformatoService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }



    public ResponseEntity<Object> getProdottoTrasformato(int id) {
        if (!this.prodottoRepository.existsProdottoTrasformatoById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.prodottoRepository.findById(id), HttpStatus.OK);
        }
    }

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
        } else {
            return ResponseEntity.status(404).body("Prodotto " + prodottoTrasformato.getId() + " Not Found");
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
