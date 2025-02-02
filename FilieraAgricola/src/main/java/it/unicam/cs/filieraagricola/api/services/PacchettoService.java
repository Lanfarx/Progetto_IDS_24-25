package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.entities.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.Prodotto;
import it.unicam.cs.filieraagricola.api.repository.PacchettoRepository;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PacchettoService {

    PacchettoRepository pacchettoRepository;
    ProdottoRepository prodottoRepository;
    public PacchettoService(PacchettoRepository pacchettoRepository, ProdottoRepository prodottoRepository) {
        this.pacchettoRepository = pacchettoRepository;
        this.prodottoRepository = prodottoRepository;
    }

    public ResponseEntity<Object> getPacchetti() {
        return new ResponseEntity<>(this.pacchettoRepository.findAll(), HttpStatus.OK);
    }


    public ResponseEntity<Object> GetPacchetto(int id) {
        if (!this.pacchettoRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.pacchettoRepository.findById(id), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> aggiungiPacchetto(Pacchetto pacchetto) {
        ResponseEntity<Object> BAD_REQUEST = getObjectResponseEntity(pacchetto.getNome(),
                pacchetto.getDescrizione(), pacchetto.getProdottiSet());
        if(BAD_REQUEST != null) return BAD_REQUEST;
        pacchettoRepository.save(pacchetto);
        return new ResponseEntity<>("Product Created", HttpStatus.CREATED);
    }

    public ResponseEntity<Object> aggiungiProdotto(int idPacchetto, int idProdotto){
        Optional<Pacchetto> pacchettoOptional = pacchettoRepository.findById(idPacchetto);
        Pacchetto pacchetto = new Pacchetto();
        if (pacchettoOptional.isPresent()) {
            pacchetto = pacchettoOptional.get();
        }
        ResponseEntity<Object> response;
        response = aggiungiProdottoAlPacchetto(pacchetto, idProdotto); //aggiunge il prodotto al pacchetto
        if(response != null) { //In caso di errore, lo mando subito
            return response;
        }
        pacchettoRepository.save(pacchetto);
        return new ResponseEntity<>("Prodotto aggiunto", HttpStatus.CREATED);
    }

    public ResponseEntity<Object> aggiungiPacchettoWithParam(String nome, String descrizione, Set<Integer> idProdottiSet){
        Set<Prodotto> prodottoSet = findSetProdotti(idProdottiSet);
        if(prodottoSet == null) {
            return new ResponseEntity<>("Un pacchetto ha bisogno di almeno 2 prodotti!", HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<Object> BAD_REQUEST = getObjectResponseEntity(nome, descrizione, prodottoSet);
        if (BAD_REQUEST != null) return BAD_REQUEST;

        Pacchetto pacchetto = new Pacchetto();
        pacchetto.setNome(nome);
        pacchetto.setDescrizione(descrizione);
        pacchetto.setProdottiSet(prodottoSet);
        this.pacchettoRepository.save(pacchetto);
        return new ResponseEntity<>("Pacchetto creato", HttpStatus.CREATED);
    }

    public ResponseEntity<Object> eliminaPacchetto(int id) {
        this.pacchettoRepository.deleteById(id);
        return new ResponseEntity<>("Product " + id + " Deleted", HttpStatus.OK);
    }

    public ResponseEntity<Object> eliminaProdotto(int id, int idProdotto) {
        if(!this.pacchettoRepository.existsById(id) || !prodottoRepository.existsById(idProdotto)) {
            return new ResponseEntity<>("Pacchetto o Prodotto inesistenti!", HttpStatus.NOT_FOUND);
        }
        Pacchetto pacchetto = pacchettoRepository.findById(idProdotto).get();
        Prodotto prodotto = prodottoRepository.findById(id).get();
        pacchetto.removeProdotto(prodotto);
        pacchettoRepository.save(pacchetto);
        return new ResponseEntity<>("Prodotto con id " + id + " Eliminato.", HttpStatus.OK);
    }

    public ResponseEntity<Object> aggiornaPacchetto(Pacchetto pacchetto) {
        if (this.pacchettoRepository.existsById(pacchetto.getId())) {
            this.pacchettoRepository.save(pacchetto);
            return new ResponseEntity<>("Pacchetto con id " + pacchetto.getId() + " Aggiornato", HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("Pacchetto con id " + pacchetto.getId() + " Non trovato");
        }
    }

    //TODO aggiungere rimozione e getter di quantita


    //--METODI INTERNI UTILI PER LA CLASSE--\\

    private ResponseEntity<Object> aggiungiProdottoAlPacchetto(Pacchetto pacchetto, int idProdotto) {
        Optional<Prodotto> prodottoOptional = prodottoRepository.findById(idProdotto);
        if(prodottoOptional.isEmpty()) {
            return new ResponseEntity<>("Product does not exist", HttpStatus.BAD_REQUEST);
        }
        Prodotto prodotto = prodottoOptional.get();
        if(pacchetto.getProdottiSet().contains(prodotto)) {
            return new ResponseEntity<>("Product already present in the bundle", HttpStatus.BAD_REQUEST);
        }
        pacchetto.addProdotto(prodotto);
        return null; //Null, così da non mostrare alcun problema (response)
    }

    private Set<Prodotto> findSetProdotti(Set<Integer> idProdottiSet) {
        if(idProdottiSet.size() < 2) {
            return null;
        }
        Set<Prodotto> prodottoSet = new HashSet<>();
        for (int i : idProdottiSet) {
            if(prodottoRepository.existsById(i)){
                if(prodottoRepository.findById(i).isPresent()){
                    prodottoSet.add(prodottoRepository.findById(i).get());
                }
            }
        }
        return prodottoSet;
    }

    private ResponseEntity<Object> getObjectResponseEntity(String nome, String descrizione, Set<Prodotto> ProdottiSet) {
        List<Pacchetto> pacchettoList = this.pacchettoRepository.findByNomeAndDescrizione(
                nome, descrizione);
        for (Pacchetto pacchetto : pacchettoList) {
            if (ProdottiSet.equals(pacchetto.getProdottiSet())) {
                return new ResponseEntity<>("Il pacchetto con gli stessi prodotti esiste già", HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

}

