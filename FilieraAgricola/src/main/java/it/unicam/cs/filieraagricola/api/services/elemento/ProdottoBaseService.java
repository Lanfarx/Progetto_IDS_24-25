package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Categoria;
import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProdottoBaseService extends ProdottoService<ProdottoBase> {

    @Autowired
    private UserService userService;

    public boolean aggiungiProdottoBase(String nome, String metodiColtivazione,
                                        String certificazioni, String descrizione,
                                        double prezzo, Categoria categoria) {

        if(prodottoRepository.existsByCaratteristicheBase(nome, metodiColtivazione, certificazioni)){
            return false;
        }
        creaBase(nome, metodiColtivazione, certificazioni, descrizione, prezzo, categoria);
        return true;
    }

    public boolean aggiungiProdottoBase(ProdottoBase prodottoBase) {
        if(prodottoRepository.existsByCaratteristicheBase(prodottoBase.getNome(), prodottoBase.getMetodiDiColtivazione(), prodottoBase.getCertificazioni())) {
            return false;
        }
        Categoria categoria = categoriaService.getCategoriaByNome(prodottoBase.getCategoria().getNome()).get();
        prodottoBase.setCategoria(categoria);
        prodottoBase.setOperatore(userService.getCurrentUser());
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

    public List<ProdottoBase> getProdottiBase() {
        return this.prodottoRepository.findProdottiBaseByOperatore(userService.getCurrentUser());
    }

    public List<ProdottoBase> getAllProdottiBaseValidi() {
        return this.prodottoRepository.findProdottiBaseByStatorichiestaEquals(StatoContenuto.ATTESA);
    }

    public void deleteProdottoBase(int id) {
        Set<Pacchetto> pacchettoSet = pacchettoService.getPacchettiConProdotto(id);
        this.prodottoRepository.deleteProdottiTrasformatiByProdottoBaseId(id);
        this.carrelloService.rimuoviDaCarrelli(id);
        this.prodottoRepository.deleteProdottoBaseById(id);
        pacchettoService.checkAndDelete(pacchettoSet);
    }

    public boolean aggiornaProdottoBase(ProdottoBase prodottoBase) {
        if (this.prodottoRepository.existsProdottoBaseById(prodottoBase.getId())) {
            prodottoBase.setOperatore(userService.getCurrentUser());
            prodottoBase.setCategoria(getProdottoBase(prodottoBase.getId()).getCategoria());
            this.prodottoRepository.save(prodottoBase);
            return true;
        } else {
            return false;
        }
    }

    public void creaBase(String nome, String metodiColtivazione, String certificazioni, String descrizone, double prezzo, Categoria categoria) {
        ProdottoBase prodottoBase = new ProdottoBase();
        prodottoBase.setNome(nome);
        prodottoBase.setMetodiDiColtivazione(metodiColtivazione);
        prodottoBase.setCertificazioni(certificazioni);
        prodottoBase.setDescrizione(descrizone);
        prodottoBase.setPrezzo(prezzo);
        prodottoBase.setOperatore(userService.getCurrentUser());
        prodottoBase.setCategoria(categoria);
        prodottoRepository.save(prodottoBase);
    }

    public boolean aggiornaQuantitaProdotto(int id, int quantita) {
        ProdottoBase prodotto = prodottoRepository.findProdottoBaseById(id).orElse(null);
        if (prodotto != null) {
            prodotto.setQuantita(prodotto.getQuantita() + quantita);
            prodottoRepository.save(prodotto);
            return true;
        }
        return false;
    }

    public boolean riduciQuantitaProdotto(int id, int quantita) {
        ProdottoBase prodotto = prodottoRepository.findProdottoBaseById(id).orElse(null);
        if (prodotto != null) {
            if (prodotto.getQuantita() >= quantita) {
                prodotto.setQuantita(prodotto.getQuantita() - quantita);
                prodottoRepository.save(prodotto);
            } else {
                deleteProdottoBase(id);
            }
            return true;
        }
        return false;
    }

    public boolean existsProdottoBase(int id) {
        return prodottoRepository.existsProdottoBaseById(id);
    }
}