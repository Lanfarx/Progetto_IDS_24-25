package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.commons.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.repository.RichiestaRuoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RichiestaRuoloService {

    @Autowired
    private RichiestaRuoloRepository richiestaRuoloRepository;

    @Autowired
    private UserService userService;

    public boolean existsRichiesta(Integer id){
        return richiestaRuoloRepository.existsById(id);
    }

    public boolean existsSameRichiesta(Users user, UserRole ruolo){
        return richiestaRuoloRepository.existsByUserAndRuoloRichiesto(user, ruolo);
    }

    public Optional<RichiestaRuolo> getRichiesta(Integer id){
        return richiestaRuoloRepository.findById(id);
    }

    public void aggiungiRichiestaRuolo(Integer userId, UserRole ruoloRichiesto) {
        Users user = userService.getUserById(userId).get();
        RichiestaRuolo richiesta = new RichiestaRuolo();
        richiesta.setUser(user);
        richiesta.setRuoloRichiesto(ruoloRichiesto);
        richiesta.setStato(StatoRichiesta.ATTESA);
        richiestaRuoloRepository.save(richiesta);
    }

    public List<RichiestaRuolo> getRichiesteInAttesa() {
        return richiestaRuoloRepository.findByStato(StatoRichiesta.ATTESA);
    }

    public RichiestaRuolo processaRichiesta(Integer richiestaId, boolean approvato) {
        if (richiestaRuoloRepository.existsById(richiestaId)) {
            RichiestaRuolo richiesta = richiestaRuoloRepository.findById(richiestaId).get();
            if (approvato) {
                richiesta.getUser().getRoles().add(richiesta.getRuoloRichiesto());
                richiesta.setStato(StatoRichiesta.ACCETTATA);
            } else {
                richiesta.setStato(StatoRichiesta.RIFIUTATA);
            }

            return richiestaRuoloRepository.save(richiesta);
        } else throw new RuntimeException("Richiesta non trovata");
    }
}
