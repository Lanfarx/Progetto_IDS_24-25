package it.unicam.cs.filieraagricola.api.services.gestore.richieste;

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

import static it.unicam.cs.filieraagricola.api.commons.richiesta.RichiestaFactory.creaRichiesta;

@Service
    public class RichiestaRuoloService implements RichiestaService<RichiestaRuolo> {

        @Autowired
        private RichiestaRepository richiestaRepository;

        @Autowired
        private UserService userService;

        @Override
        public void aggiungiRichiesta(Integer userId, Object ruoloRichiesto) {
            Users user = userService.getUserById(userId).get();
            richiestaRepository.save(creaRichiesta("RUOLO", user, ruoloRichiesto));
        }

        @Override
        public boolean existsRichiesta(Integer id) {
            return richiestaRepository.existsById(id);
        }

        @Override
        public boolean existsSameRichiesta(Users user, Object ruolo) {
            return richiestaRepository.existsRichiestRuoloByUserAndRuoloRichiesto(user, (UserRole) ruolo);
        }

        @Override
        public Optional<RichiestaRuolo> getRichiesta(Integer id) {
            return richiestaRepository.findRichiestaRuoloById(id);
        }

        @Override
        public List<RichiestaRuolo> getRichiesteInAttesa() {
            return richiestaRepository.findRichiestaRuoloByStato(StatoRichiesta.ATTESA);
        }

        @Override
        public void processaRichiesta(Integer richiestaId, boolean approvato) {
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
                richiestaRepository.save(richiesta);
            } else throw new RuntimeException("Richiesta non trovata");
        }
    }

