package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.entities.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.Prodotto;
import it.unicam.cs.filieraagricola.api.repository.PacchettoRepository;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
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

    public List<Pacchetto> getPacchetti() {
        return this.pacchettoRepository.findAll();
    }


    public Pacchetto getPacchetto(int id) {
        if (!this.pacchettoRepository.existsById(id)) {
            return pacchettoRepository.findById(id).get();
        } else {
            return null;
        }
    }

    public boolean aggiungiPacchetto(Pacchetto pacchetto) {
        boolean BAD_REQUEST = getObjectResponseEntity(pacchetto.getNome(),
                pacchetto.getDescrizione(), pacchetto.getProdottiSet());
        if(!BAD_REQUEST) return false;
        pacchettoRepository.save(pacchetto);
        return true;
    }

    public boolean aggiungiProdotto(int idPacchetto, int idProdotto){
        Optional<Pacchetto> pacchettoOptional = pacchettoRepository.findById(idPacchetto);
        Pacchetto pacchetto = new Pacchetto();
        if (pacchettoOptional.isPresent()) {
            pacchetto = pacchettoOptional.get();
        }
        boolean response = aggiungiProdottoAlPacchetto(pacchetto, idProdotto); //aggiunge il prodotto al pacchetto
        if(!response) { //In caso di errore, lo mando subito
            return false;
        }
        pacchettoRepository.save(pacchetto);
        return true;
    }

    public boolean aggiungiPacchettoWithParam(String nome, String descrizione, Set<Integer> idProdottiSet){
        Set<Prodotto> prodottoSet = findSetProdotti(idProdottiSet);
        if(prodottoSet == null || prodottoSet.size() < 2) {
            return false;
        }
        boolean BAD_REQUEST = getObjectResponseEntity(nome, descrizione, prodottoSet);
        if (!BAD_REQUEST) return false;

        Pacchetto pacchetto = new Pacchetto();
        pacchetto.setNome(nome);
        pacchetto.setDescrizione(descrizione);
        pacchetto.setProdottiSet(prodottoSet);
        this.pacchettoRepository.save(pacchetto);
        return true;
    }

    public void eliminaPacchetto(int id) { this.pacchettoRepository.deleteById(id); }

    public boolean eliminaProdotto(int id, int idProdotto) {
        if(!this.pacchettoRepository.existsById(id) || !prodottoRepository.existsById(idProdotto)) {
            return false;
        }
        Pacchetto pacchetto = pacchettoRepository.findById(idProdotto).get();
        Prodotto prodotto = prodottoRepository.findById(id).get();
        pacchetto.removeProdotto(prodotto);
        if(pacchetto.getProdottiSet().size() < 2){
            eliminaPacchetto(id);
        } else {
            pacchettoRepository.save(pacchetto);
        }
        return true;
    }

    public Set<Pacchetto> getPacchettiConProdotto(int idProdotto) {
       return pacchettoRepository.findPacchettiByProdottoId(idProdotto);
    }

    public boolean aggiornaPacchetto(Pacchetto pacchetto) {
        if (this.pacchettoRepository.existsById(pacchetto.getId())) {
            this.pacchettoRepository.save(pacchetto);
            return true;
        } else {
            return false;
        }
    }

    public void checkAndDelete(Set<Pacchetto> pacchettoList) {
        for (Pacchetto pacchetto : pacchettoList) {
            if(pacchetto.getProdottiSet().isEmpty()){
                eliminaPacchetto(pacchetto.getId());
            }
        }
    }

    //TODO aggiungere rimozione e getter di quantita


    //--METODI INTERNI UTILI PER LA CLASSE--\\

    private boolean aggiungiProdottoAlPacchetto(Pacchetto pacchetto, int idProdotto) {
        Optional<Prodotto> prodottoOptional = prodottoRepository.findById(idProdotto);
        if(prodottoOptional.isEmpty()) {
            return false;
        }
        Prodotto prodotto = prodottoOptional.get();
        if(pacchetto.getProdottiSet().contains(prodotto)) {
            return false;
        }
        pacchetto.addProdotto(prodotto);
        return true; //Null, così da non mostrare alcun problema (response)
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

    private boolean getObjectResponseEntity(String nome, String descrizione, Set<Prodotto> ProdottiSet) {
        List<Pacchetto> pacchettoList = this.pacchettoRepository.findByNomeAndDescrizione(
                nome, descrizione);
        for (Pacchetto pacchetto : pacchettoList) {
            if (ProdottiSet.equals(pacchetto.getProdottiSet())) {
                return false;
            }
        }
        return true;
    }

}