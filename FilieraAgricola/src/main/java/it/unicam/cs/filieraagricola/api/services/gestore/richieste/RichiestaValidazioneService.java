package it.unicam.cs.filieraagricola.api.services.gestore.richieste;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.commons.richiesta.TipoRichiesta;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import it.unicam.cs.filieraagricola.api.repository.ElementoRepository;
import it.unicam.cs.filieraagricola.api.repository.RichiestaRepository;
import it.unicam.cs.filieraagricola.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static it.unicam.cs.filieraagricola.api.commons.richiesta.RichiestaFactory.creaRichiesta;

@Service
public class RichiestaValidazioneService extends AbstractRichiestaService<RichiestaValidazione> {

    @Autowired
    private ElementoRepository elementoRepository;

    @Autowired
    private UserService userService;

    @Override
    public void aggiungiRichiesta(Integer userId, Object valore) {
        Users user = userService.getUserById(userId).get();
        richiestaRepository.save(creaRichiesta(TipoRichiesta.VALIDAZIONE, user, valore));
    }

    @Override
    public boolean existsRichiesta(Integer id) {
        return richiestaRepository.existsRichiestaValidazioneById(id);
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
        return richiestaRepository.findRichiestaValidazioneByStato(StatoContenuto.ATTESA);
    }

    public List<RichiestaValidazione> getMieRichiesteValidazione(Users currentUser){
        return richiestaRepository.findRichiesteValidazioneByUser(currentUser);
    }

    @Override
    public void processaRichiesta(Integer richiestaId, boolean approvato) {
        if (richiestaRepository.existsRichiestaValidazioneById(richiestaId)) {
            RichiestaValidazione richiesta = (RichiestaValidazione) richiestaRepository.findById(richiestaId).get();
            Elemento elemento = richiesta.getElemento();

            richiesta.setStato(approvato ? StatoContenuto.ACCETTATA : StatoContenuto.RIFIUTATA);
            elemento.setStatorichiesta(richiesta.getStato());

            elementoRepository.save(elemento);
            richiestaRepository.save(richiesta);
        } else throw new RuntimeException("Richiesta non trovata");
    }
}
