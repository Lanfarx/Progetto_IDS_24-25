package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProdottoBaseService extends ProdottoService<ProdottoBase> {

    public boolean aggiungiProdottoBase(String nome, String metodiColtivazione,
                                                       String certificazioni, String descrizione, double prezzo) {
        if(prodottoRepository.existsByCaratteristicheBase(nome, metodiColtivazione, certificazioni)){
            return false;
        }
        creaBase(nome, metodiColtivazione, certificazioni, descrizione, prezzo);
        return true;
    }

    public boolean aggiungiProdottoBase(ProdottoBase prodottoBase) {
        if(prodottoRepository.existsById(prodottoBase.getId())) {
            return false;
        }
        prodottoRepository.save(prodottoBase);
        return true;
    }

    public ProdottoBase getProdottoBase(int id) {
        if (this.prodottoRepository.existsProdottoBaseById(id)) {
            return prodottoRepository.findProdottoBaseById(id).get();
        } else {
            return null;
        }
    }

    public List<ProdottoBase> getProdottiBase() {
        return this.prodottoRepository.findAllProdottiBase();
    }

    public void deleteProdottoBase(int id) {
        Set<Pacchetto> pacchettoSet = pacchettoService.getPacchettiConProdotto(id);
        this.prodottoRepository.deleteProdottiTrasformatiByProdottoBaseId(id);
        this.prodottoRepository.deleteProdottoBaseById(id);
        pacchettoService.checkAndDelete(pacchettoSet);
    }

    public boolean aggiornaProdottoBase(ProdottoBase prodottoBase) {
        if (this.prodottoRepository.existsProdottoBaseById(prodottoBase.getId())) {
            this.prodottoRepository.save(prodottoBase);
            return true;
        } else {
            return false;
        }
    }

    public void creaBase(String nome, String metodiColtivazione, String certificazioni, String descrizone, double prezzo) {
        ProdottoBase prodottoBase = new ProdottoBase();
        prodottoBase.setNome(nome);
        prodottoBase.setMetodiDiColtivazione(metodiColtivazione);
        prodottoBase.setCertificazioni(certificazioni);
        prodottoBase.setDescrizione(descrizone);
        prodottoBase.setPrezzo(prezzo);
        prodottoRepository.save(prodottoBase);
    }
}