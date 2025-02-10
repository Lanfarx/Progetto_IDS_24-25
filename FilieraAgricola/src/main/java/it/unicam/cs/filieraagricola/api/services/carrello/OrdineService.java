package it.unicam.cs.filieraagricola.api.services.carrello;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.ElementoCarrello;
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
        List<ElementoCarrello> nuoviElementi = getElementiOrdine(carrello, ordine);
        ordine.setElementi(nuoviElementi);
        ordine.setPrezzoTotale(carrello.getPrezzoTotale());
        ordine.setDataOrdine(LocalDateTime.now());
        ordineRepository.save(ordine);

        pulisciCarrello(carrello);
        return ordine;
    }

    private List<ElementoCarrello> getElementiOrdine(Carrello carrello, Ordine ordine) {
        List<ElementoCarrello> nuoviElementi = new ArrayList<>();
        for (ElementoCarrello elemento : carrello.getElementi()) {
            ElementoCarrello nuovoElemento = new ElementoCarrello();
            nuovoElemento.setElemento(elemento.getElemento());
            nuovoElemento.setQuantita(elemento.getQuantita());
            nuovoElemento.setPrezzoTotale(elemento.getPrezzoTotale());
            nuovoElemento.setOrdine(ordine);
            nuoviElementi.add(nuovoElemento);
        }
        return nuoviElementi;
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
