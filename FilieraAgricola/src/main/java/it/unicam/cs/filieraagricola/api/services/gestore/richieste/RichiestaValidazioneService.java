package it.unicam.cs.filieraagricola.api.services.gestore.richieste;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.entities.Elemento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import it.unicam.cs.filieraagricola.api.repository.ElementoRepository;
import it.unicam.cs.filieraagricola.api.repository.RichiestaRepository;
import it.unicam.cs.filieraagricola.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RichiestaValidazioneService implements RichiestaService<RichiestaValidazione> {

    @Autowired
    private RichiestaRepository richiestaRepository;

    @Autowired
    private ElementoRepository elementoRepository;

    @Autowired
    private UserService userService;

    @Override
    public void aggiungiRichiesta(Integer userId, Object valore) {
        if (!(valore instanceof Elemento)) {
            throw new IllegalArgumentException("Il valore deve essere un'istanza di Elemento.");
        }
        Users user = userService.getUserById(userId).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        Elemento elemento = (Elemento) valore;

        if (existsSameRichiesta(user, elemento)) {
            throw new RuntimeException("Esiste già una richiesta di validazione per questo elemento.");
        }

        RichiestaValidazione richiestaValidazione = new RichiestaValidazione();
        richiestaValidazione.setUser(user);
        richiestaValidazione.setElemento(elemento);
        richiestaValidazione.setStato(StatoRichiesta.ATTESA);

        richiestaRepository.save(richiestaValidazione);
    }

    @Override
    public boolean existsRichiesta(Integer id) {
        return richiestaRepository.existsById(id);
    }

    @Override
    public boolean existsSameRichiesta(Users user, Object valore) {
        return richiestaRepository.existsRichiestaValidazioneByUserAndElemento(user, (Elemento) valore);

    }

    @Override
    public Optional<RichiestaValidazione> getRichiesta(Integer id) {
        return richiestaRepository.findRichiestaValidazioneById(id);
    }

    @Override
    public List<RichiestaValidazione> getRichiesteInAttesa() {
        return richiestaRepository.findRichiestaValidazioneByStato(StatoRichiesta.ATTESA);
    }

    @Override
    public void processaRichiesta(Integer richiestaId, boolean approvato) {
        RichiestaValidazione richiestaValidazione = richiestaRepository.findRichiestaValidazioneById(richiestaId)
                .orElseThrow(() -> new RuntimeException("Richiesta di validazione non trovata"));

        Elemento elemento = richiestaValidazione.getElemento();

        if (approvato) {
            elemento.setStatoRichiesta(StatoRichiesta.ACCETTATA);
        } else {
            elemento.setStatoRichiesta(StatoRichiesta.RIFIUTATA);
        }

        richiestaValidazione.setStato(approvato ? StatoRichiesta.ACCETTATA : StatoRichiesta.RIFIUTATA);

        elementoRepository.save(elemento);
        richiestaRepository.save(richiestaValidazione);
    }
}
