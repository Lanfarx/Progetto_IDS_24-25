package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Prodotto;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.api.services.carrello.CarrelloService;
import it.unicam.cs.filieraagricola.api.services.gestore.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdottoService<T extends Prodotto> extends ElementoService<Prodotto>{

    @Autowired
    protected ProdottoRepository prodottoRepository;

    public List<T> getProdottiValidi() {
        return (List<T>) prodottoRepository.findByStatorichiestaEquals(StatoContenuto.ACCETTATA);
    }

    public Prodotto getProdottoById(int idProdotto) {
        return prodottoRepository.findById(idProdotto).get();
    }

    public boolean existsProdotto(int idProdotto) {
        return prodottoRepository.existsById(idProdotto);
    }



    public boolean aggiornaQuantitaProdotto(int id, int quantita){
        Prodotto prodotto = prodottoRepository.findProdottoById(id);
        if (prodotto != null) {
            prodotto.setQuantita(prodotto.getQuantita() + quantita);
            prodottoRepository.save(prodotto);
            return true;
        }
        return false;
    }

    public boolean riduciQuantitaProdotto(int id, int quantita){
        Prodotto prodotto = prodottoRepository.findProdottoById(id);
        if (prodotto != null) {
            if (prodotto.getQuantita() >= quantita) {
                prodotto.setQuantita(prodotto.getQuantita() - quantita);
                prodottoRepository.save(prodotto);
            } else {
                prodottoRepository.deleteProdottoById(id);
            }
            return true;
        }
        return false;
    }
}
