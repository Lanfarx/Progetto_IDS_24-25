package it.unicam.cs.filieraagricola.api.services.carrello;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.ElementoCarrello;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.repository.CarrelloRepository;
import it.unicam.cs.filieraagricola.api.repository.ElementoCarrelloRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private ElementoCarrelloRepository elementoCarrelloRepository;

    public Carrello getCarrello(Users currentUser) {
        return ottieniCarrello(currentUser);
    }

    public void aggiungiAlCarrello(Users currentUser, Elemento elemento, int quantita) {
        Carrello carrello = ottieniCarrello(currentUser);

        if (contieneElemento(carrello, elemento)) {
            for (ElementoCarrello elementoCarrello : carrello.getElementi())
                if (elementoCarrello.getElemento().equals(elemento)) {
                    elementoCarrello.setQuantita(elementoCarrello.getQuantita() + quantita);
                    elementoCarrelloRepository.save(elementoCarrello);
                    break;
                }
        } else {
            ElementoCarrello nuovoElementoCarrello = new ElementoCarrello(carrello, elemento, quantita);
            elementoCarrelloRepository.save(nuovoElementoCarrello);
            carrello.getElementi().add(nuovoElementoCarrello);
        }
        rimuoviQuantita(elemento, quantita);
        aggiornaPrezzo(carrello);
        carrelloRepository.save(carrello);
    }

    private Carrello ottieniCarrello(Users currentUser) {
        Carrello carrello = carrelloRepository.findByUserId(currentUser.getId());
        if (carrello == null) {
            carrello = new Carrello();
            carrello.setUser(currentUser);
            carrello = carrelloRepository.save(carrello);
        }
        return carrello;
    }

    private void rimuoviQuantita(Elemento elemento, int quantita) {
        elemento.removeQuantita(quantita);
    }

    private void aggiornaPrezzo(Carrello carrello) {
        double totale = 0;
        for (ElementoCarrello elementoCarrello : carrello.getElementi())
            totale += elementoCarrello.getPrezzoTotale();
        carrello.setPrezzoTotale(totale);
    }

    public boolean contieneElemento(Carrello carrello, Elemento elemento) {
        for (ElementoCarrello elementoCarrello : carrello.getElementi())
            if (elementoCarrello.getElemento().equals(elemento)) {
                return true;
            }
        return false;
    }

    public void rimuoviDalCarrello(Users currentUser, Elemento elemento, int quantita) {
        Carrello carrello = carrelloRepository.findByUserId(currentUser.getId());

        for (int i = 0; i < carrello.getElementi().size(); i++) {
            ElementoCarrello elementoCarrello = carrello.getElementi().get(i);

            if (elementoCarrello.getElemento().equals(elemento)) {
                int quantitaAttuale = elementoCarrello.getQuantita();
                if (quantita >= quantitaAttuale) {
                    carrello.getElementi().remove(i);
                } else {
                    elementoCarrello.rimuoviQuantita(quantita);
                }
                elemento.aggiungiQuantita(quantita);
                break;
            }
        }
        aggiornaPrezzo(carrello);
        carrelloRepository.save(carrello);
    }

    public void rimuoviDaCarrelli(int id) {
        carrelloRepository.rimuoviElementoDaCarrelli(id);
        List<Carrello> carrelli = carrelloRepository.findAll();
        for (Carrello carrello : carrelli) {
            aggiornaPrezzo(carrello);
        }
    }
}
