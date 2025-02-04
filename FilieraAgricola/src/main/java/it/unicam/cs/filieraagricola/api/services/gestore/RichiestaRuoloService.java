package it.unicam.cs.filieraagricola.api.services.gestore;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.repository.RichiestaRepository;
import it.unicam.cs.filieraagricola.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RichiestaRuoloService {

    @Autowired
    private RichiestaRepository richiestaRepository;

    @Autowired
    private UserService userService;

    public void aggiungiRichiesta(Integer userId, UserRole ruoloRichiesto) {
        Users user = userService.getUserById(userId).get();
        RichiestaRuolo richiesta = new RichiestaRuolo();
        richiesta.setUser(user);
        richiesta.setRuoloRichiesto(ruoloRichiesto);
        richiesta.setStato(StatoRichiesta.ATTESA);
        richiestaRepository.save(richiesta);
    }

    public boolean existsRichiesta(Integer id){
        return richiestaRepository.existsById(id);
    }
    public boolean existsSameRichiesta(Users user, UserRole ruolo){
        return richiestaRepository.existsRichiestRuoloByUserAndRuoloRichiesto(user, ruolo);
    }
    public Optional<RichiestaRuolo> getRichiesta(Integer id){
        return richiestaRepository.findRichiestaRuoloById(id);
    }
    public List<RichiestaRuolo> getRichiesteInAttesa() {
        return richiestaRepository.findRichiestaRuoloByStato(StatoRichiesta.ATTESA);
    }

    public RichiestaRuolo processaRichiesta(Integer richiestaId, boolean approvato) {
        if (richiestaRepository.existsRichiestaRuoloById(richiestaId)) {
            RichiestaRuolo richiesta = (RichiestaRuolo) richiestaRepository.findById(richiestaId).get();
            if (approvato) {
                Users user = richiesta.getUser();
                user.getRoles().add(richiesta.getRuoloRichiesto());
                userService.save(user);
                richiesta.setStato(StatoRichiesta.ACCETTATA);
            } else {
                richiesta.setStato(StatoRichiesta.RIFIUTATA);
            }
            return richiestaRepository.save(richiesta);
        } else throw new RuntimeException("Richiesta non trovata");
    }
}