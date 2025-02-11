package it.unicam.cs.filieraagricola.api.services.carrello;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.ElementoCarrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.ElementoOrdine;
import it.unicam.cs.filieraagricola.api.entities.carrello.Ordine;
import it.unicam.cs.filieraagricola.api.repository.CarrelloRepository;
import it.unicam.cs.filieraagricola.api.repository.OrdineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdineService {

    @Autowired
    private OrdineRepository ordineRepository;
    @Autowired
    private CarrelloRepository carrelloRepository;


    public Ordine creaOrdine(Carrello carrello){
        Ordine ordine = new Ordine();
        ordine.setUser(carrello.getUser());
        ordine.setDataOrdine(LocalDateTime.now());

        List<ElementoOrdine> dettagliOrdine = getElementiOrdine(carrello, ordine);
        ordine.setElementi(dettagliOrdine);

        ordine.setPrezzoTotale(carrello.getPrezzoTotale());
        ordineRepository.save(ordine);

        pulisciCarrello(carrello);
        return ordine;
    }

    private List<ElementoOrdine> getElementiOrdine(Carrello carrello, Ordine ordine) {
        List<ElementoOrdine> elementiOrdine = new ArrayList<>();
        for (ElementoCarrello elemento : carrello.getElementi()) {
            ElementoOrdine elementoOrdine = new ElementoOrdine(
                    elemento.getQuantita(), elemento.getElemento().getPrezzo(),
                    elemento.getElemento().getDescrizione(), elemento.getElemento().getNome()
            );
            elementoOrdine.setOrdine(ordine);
            elementiOrdine.add(elementoOrdine);
        }
        return elementiOrdine;
    }

    private void pulisciCarrello(Carrello carrello) {
        carrello.getElementi().clear();
        carrello.setPrezzoTotale(0);
        carrelloRepository.save(carrello);
    }

    public List<Ordine> getOrdini(Users currentUser) {
        return ordineRepository.findByUserId(currentUser.getId());
    }
}
