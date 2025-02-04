package it.unicam.cs.filieraagricola.api.services.gestore;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.repository.RichiestaRepository;
import it.unicam.cs.filieraagricola.api.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RichiestaEliminazioneService {

    @Autowired
    private RichiestaRepository richiestaRepository;

    @Autowired
    private UserService userService;

    public void aggiungiRichiesta(Integer userId, String motivazione) {
        Users user = userService.getUserById(userId).get();
        RichiestaEliminazione richiesta = new RichiestaEliminazione();
        richiesta.setUser(user);
        richiesta.setMotivazione(motivazione);
        richiesta.setStato(StatoRichiesta.ATTESA);
        richiestaRepository.save(richiesta);
    }

    public boolean existsRichiesta(Integer id){
        return richiestaRepository.existsRichiestaEliminazioneById(id);
    }

    public boolean existsSameRichiesta(Users user){
        return richiestaRepository.existsRichiestaEliminazioneByUserAndMotivazione(user);
    }

    public Optional<RichiestaEliminazione> getRichiesta(Integer id){
        return richiestaRepository.findRichiestaEliminazioneById(id);
    }

    public List<RichiestaEliminazione> getRichiesteInAttesa() {
        return richiestaRepository.findRichiestaEliminazioneByStato(StatoRichiesta.ATTESA);
    }

    public void processaRichiesta(Integer richiestaId, boolean approvato) {
        if (richiestaRepository.existsRichiestaEliminazioneById(richiestaId)) {
            RichiestaEliminazione richiesta = (RichiestaEliminazione) richiestaRepository.findById(richiestaId).get();
            if (approvato) {
                userService.delete(richiesta.getUser());
            } else {
                richiesta.setStato(StatoRichiesta.RIFIUTATA);
                richiestaRepository.save(richiesta);
            }
        } else throw new RuntimeException("Richiesta non trovata");
    }
}