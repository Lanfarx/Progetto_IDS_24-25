package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProdottoTrasformatoService extends ProdottoService<ProdottoTrasformato> {

    public ProdottoTrasformato getProdottoTrasformato(int id) {
        if (!this.prodottoRepository.existsProdottoTrasformatoById(id)) {
            return prodottoRepository.findProdottoTrasformatoById(id).get();
        } else {
            return null;
        }
    }

    public List<ProdottoTrasformato> getProdottiTrasformati() {
        return prodottoRepository.findAllProdottiTrasformati();
    }

    public void deleteProdottoTrasformato(int id) {

        Set<Pacchetto> pacchettoSet = pacchettoService.getPacchettiConProdotto(id);
        this.prodottoRepository.deleteById(id);
        pacchettoService.checkAndDelete(pacchettoSet);
    }

    public boolean aggiornaProdottoTrasformato(ProdottoTrasformato prodottoTrasformato) {
        if (this.prodottoRepository.existsProdottoTrasformatoById(prodottoTrasformato.getId())) {
            this.prodottoRepository.save(prodottoTrasformato);
            return true;
        } else {
            return false;
        }
    }

    public boolean aggiungiProdottoTrasformato(String nome, String processo,
                                                              String certificazioni, int prodottoBaseID,
                                                              String descrizione, double prezzo) {

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
        creaTrasformato(nome, processo, certificazioni, prodottoBaseID, descrizione, prezzo);
        return true;
    }

    public boolean aggiungiProdottoTrasformato(ProdottoTrasformato prodottoTrasformato) {
        if(prodottoRepository.existsById(prodottoTrasformato.getId())) {
            return false;
        }
        prodottoRepository.save(prodottoTrasformato);
        return true;
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
    }
}