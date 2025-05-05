package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoTrasformato;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdottoTrasformatoService extends ProdottoService<ProdottoTrasformato> {

    public ProdottoTrasformato getProdottoTrasformato(int id) {
        if (this.prodottoRepository.existsProdottoTrasformatoById(id)) {
            return prodottoRepository.findProdottoTrasformatoById(id).get();
        } else {
            return null;
        }
    }

    public List<ProdottoTrasformato> getProdottiTrasformati(Users currentUser) {
        return prodottoRepository.findProdottiTrasformatiByOperatore(currentUser);
    }

    public List<ProdottoTrasformato> getAllProdottiTrasformatiValidi() {
        return prodottoRepository.findProdottiTrasformatoByStatorichiestaEquals(StatoContenuto.ACCETTATA);
    }

    public void deleteProdottoTrasformato(int id) {
        this.prodottoRepository.deleteById(id);
    }

    public boolean aggiornaProdottoTrasformato(ProdottoTrasformato prodottoTrasformato, Users currentUser) {
        if (this.prodottoRepository.existsProdottoTrasformatoById(prodottoTrasformato.getId())) {
            prodottoTrasformato.setOperatore(currentUser);
            prodottoTrasformato.setCategoria(getProdottoTrasformato(prodottoTrasformato.getId()).getCategoria());
            this.prodottoRepository.save(prodottoTrasformato);
            return true;
        } else {
            return false;
        }
    }

    public boolean aggiungiProdottoTrasformato(String nome, String processo,
                                               String certificazioni, int prodottoBaseID,
                                               String descrizione, double prezzo, String categoria, Users currentUser) {
        Optional<ProdottoBase> prodottoBaseOpt = prodottoRepository.findProdottoBaseById(prodottoBaseID);
        ProdottoBase prodottoBase;
        if(prodottoBaseOpt.isPresent()) {
            prodottoBase = prodottoBaseOpt.get();
        } else {
            return false;
        }
        if(prodottoRepository.existsByCaratteristicheTrasformato(nome, processo, certificazioni, prodottoBase)){
            return false;
        }
        creaTrasformato(nome, processo, certificazioni, prodottoBaseID, descrizione, prezzo, categoria, currentUser);
        return true;
    }

    public boolean aggiungiProdottoTrasformato(ProdottoTrasformato prodottoTrasformato, Users currentUser) {
        if(prodottoRepository.existsByCaratteristicheBase(prodottoTrasformato.getNome(), prodottoTrasformato.getProcessoTrasformazione(),prodottoTrasformato.getCertificazioni())) {
            return false;
        }
        prodottoTrasformato.setOperatore(currentUser);
        prodottoRepository.save(prodottoTrasformato);
        return true;
    }


    private void creaTrasformato(String nome, String processo, String certificazioni, int prodottoBaseID,
                                String descrizione, double prezzo, String categoria, Users currentUser) {
        ProdottoTrasformato prodottoTrasformato = new ProdottoTrasformato();
        ProdottoBase prodottoBase = prodottoRepository.findProdottoBaseById(prodottoBaseID).get(); //Il controllo per esistenza viene già effettuato in esisteProdottoTrasformato
        prodottoTrasformato.setNome(nome);
        prodottoTrasformato.setProcessoTrasformazione(processo);
        prodottoTrasformato.setCertificazioni(certificazioni);
        prodottoTrasformato.setProdottoBase(prodottoBase);
        prodottoTrasformato.setDescrizione(descrizione);
        prodottoTrasformato.setPrezzo(prezzo);
        prodottoTrasformato.setOperatore(currentUser);
        prodottoTrasformato.setCategoria(categoria);
        prodottoRepository.save(prodottoTrasformato);
    }

    public boolean existsProdottoTrasformato(int id) {
        return prodottoRepository.existsProdottoTrasformatoById(id);
    }
}