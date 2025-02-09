package it.unicam.cs.filieraagricola.api.services.carrello;

import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.Ordine;
import it.unicam.cs.filieraagricola.api.repository.CarrelloRepository;
import it.unicam.cs.filieraagricola.api.repository.OrdineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class OrdineService {

    @Autowired
    private OrdineRepository ordineRepository;
    @Autowired
    private CarrelloRepository carrelloRepository;

    @Transactional
    public Ordine creaOrdine(Carrello carrello){
        Ordine ordine = new Ordine();
        ordine.setUser(carrello.getUser());
        ordine.setElementi(new ArrayList<>(carrello.getElementi()));
        ordine.setPrezzoTotale(carrello.getPrezzoTotale());
        ordine.setDataOrdine(LocalDateTime.now());
        ordineRepository.save(ordine);

        pulisciCarrello(carrello);
        return ordine;
    }

    private void pulisciCarrello(Carrello carrello) {
        carrello.getElementi().clear();
        carrello.setPrezzoTotale(0);
        carrelloRepository.save(carrello);
    }
}
