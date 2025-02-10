package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Prodotto;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.api.services.gestore.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdottoService<T extends Prodotto> extends ElementoService<Prodotto>{

    @Autowired
    protected ProdottoRepository prodottoRepository;
    @Autowired
    protected PacchettoService pacchettoService;
    @Autowired
    protected CategoriaService categoriaService;

    public List<T> getProdottiValidi() {
        return (List<T>) prodottoRepository.findByStatorichiestaEquals(StatoContenuto.ACCETTATA);
    }

    public Prodotto getProdottoById(int idProdotto) {
        return prodottoRepository.findById(idProdotto).get();
    }

    public boolean existsProdotto(int idProdotto) {
        return prodottoRepository.existsById(idProdotto);
    }
}
