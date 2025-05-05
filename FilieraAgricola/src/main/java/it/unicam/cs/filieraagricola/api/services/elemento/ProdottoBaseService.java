package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProdottoBaseService extends ProdottoService<ProdottoBase> {

    public boolean aggiungiProdottoBase(String nome, String metodiColtivazione,
                                        String certificazioni, String descrizione,
                                        double prezzo, String categoria, Users currentUser) {

        if(prodottoRepository.existsByCaratteristicheBase(nome, metodiColtivazione, certificazioni)){
            return false;
        }
        creaBase(nome, metodiColtivazione, certificazioni, descrizione, prezzo, categoria, currentUser);
        return true;
    }

    public boolean aggiungiProdottoBase(ProdottoBase prodottoBase, Users currentUser, String categoria) {
        if(prodottoRepository.existsByCaratteristicheBase(prodottoBase.getNome(), prodottoBase.getMetodiDiColtivazione(), prodottoBase.getCertificazioni())) {
            return false;
        }
        prodottoBase.setCategoria(categoria);
        prodottoBase.setOperatore(currentUser);
        prodottoRepository.save(prodottoBase);
        return true;
    }

    public ProdottoBase getProdottoBase(int id) {
        if (this.prodottoRepository.existsProdottoBaseById(id) ) {
            return prodottoRepository.findProdottoBaseById(id).get();
        } else {
            return null;
        }
    }

    public List<ProdottoBase> getProdottiBase(Users currentUser) {
        return this.prodottoRepository.findProdottiBaseByOperatore(currentUser);
    }

    public List<ProdottoBase> getAllProdottiBaseValidi() {
        return this.prodottoRepository.findProdottiBaseByStatorichiestaEquals(StatoContenuto.ATTESA);
    }
    public List<ProdottoBase> getAllProdottiBaseByUser(Users user){
        return this.prodottoRepository.findProdottiBaseByOperatore(user);
    }

    @Transactional
    public void deleteProdottoBase(int id) {
        this.prodottoRepository.deleteProdottiTrasformatiByProdottoBaseId(id);
        this.prodottoRepository.deleteProdottoBaseById(id);
    }

    public boolean aggiornaProdottoBase(ProdottoBase prodottoBase, Users currentUser) {
        if (this.prodottoRepository.existsProdottoBaseById(prodottoBase.getId())) {
            prodottoBase.setOperatore(currentUser);
            prodottoBase.setCategoria(getProdottoBase(prodottoBase.getId()).getCategoria());
            this.prodottoRepository.save(prodottoBase);
            return true;
        } else {
            return false;
        }
    }

    private void creaBase(String nome, String metodiColtivazione, String certificazioni,
                          String descrizone, double prezzo, String categoria, Users currentUser) {
        ProdottoBase prodottoBase = new ProdottoBase();
        prodottoBase.setNome(nome);
        prodottoBase.setMetodiDiColtivazione(metodiColtivazione);
        prodottoBase.setCertificazioni(certificazioni);
        prodottoBase.setDescrizione(descrizone);
        prodottoBase.setPrezzo(prezzo);
        prodottoBase.setOperatore(currentUser);
        prodottoBase.setCategoria(categoria);
        prodottoRepository.save(prodottoBase);
    }

    public boolean existsProdottoBase(int id) {
        return prodottoRepository.existsProdottoBaseById(id);
    }

}