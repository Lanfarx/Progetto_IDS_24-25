package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.entities.elemento.Prodotto;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdottoService<T extends Prodotto> extends ElementoService<Prodotto>{

    @Autowired
    protected ProdottoRepository prodottoRepository;
    @Autowired
    protected PacchettoService pacchettoService;

    public List<T> getProdotti() {
        return (List<T>) prodottoRepository.findAll();
    }

    public boolean existsProdotto(int id) {
        return prodottoRepository.existsById(id);
    }
}
