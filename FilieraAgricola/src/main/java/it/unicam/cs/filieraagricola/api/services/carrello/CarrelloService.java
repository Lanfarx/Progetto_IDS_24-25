package it.unicam.cs.filieraagricola.api.services.carrello;

import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.ElementoCarrello;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.repository.CarrelloRepository;
import it.unicam.cs.filieraagricola.api.repository.ElementoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRepository;

    public Carrello getCarrello(int id) {
        return carrelloRepository.findByUserId(id);
    }

    @Transactional
    public void aggiungiAlCarrello(int userId, Elemento elemento, int quantita) {
        Carrello carrello = carrelloRepository.findByUserId(userId);

        if (contieneElemento(carrello, elemento)) {
            for (ElementoCarrello cartItem : carrello.getElementi())
                if (cartItem.getElemento().equals(elemento)) {
                    cartItem.setQuantita(cartItem.getQuantita() + quantita);
                    break;
                }
        } else carrello.getElementi().add(new ElementoCarrello(elemento, quantita));
        aggiornaPrezzo(carrello);
        carrelloRepository.save(carrello);
    }

    private void aggiornaPrezzo(Carrello carrello) {
        double totale = 0;
        for (ElementoCarrello elementoCarrello : carrello.getElementi())
            totale += elementoCarrello.getPrezzoTotale();
        carrello.setPrezzoTotale(totale);
    }

    private boolean contieneElemento(Carrello carrello, Elemento elemento) {
        for (ElementoCarrello elementoCarrello : carrello.getElementi())
            if (elementoCarrello.getElemento().equals(elemento)) {
                return true;
            }
        return false;
    }

    @Transactional
    public void rimuoviDalCarrello(int userId, Elemento elemento) {
        Carrello carrello = carrelloRepository.findByUserId(userId);

        for (ElementoCarrello elementoCarrello : carrello.getElementi())
            if (elementoCarrello.getElemento().equals(elemento)) {
                carrello.getElementi().remove(elementoCarrello);
                break;
            }
        aggiornaPrezzo(carrello);
        carrelloRepository.save(carrello);

    }


}
