package it.unicam.cs.filieraagricola.api.services.gestore.richieste;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.commons.richiesta.TipoRichiesta;
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
    public class RichiestaRuoloService extends AbstractRichiestaService<RichiestaRuolo> {

        @Autowired
        private UserService userService;

        @Override
        public void aggiungiRichiesta(Users user, Object ruoloRichiesto) {
            richiestaRepository.save(creaRichiesta(TipoRichiesta.RUOLO, user, ruoloRichiesto));
        }

        @Override
        public boolean existsRichiesta(Integer id) {
            return richiestaRepository.existsRichiestaRuoloById(id);
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
            return richiestaRepository.findRichiestaRuoloByStato(StatoContenuto.ATTESA);
        }

        public boolean userAlreadyHasRuolo(Users user, UserRole ruolo) {
            return user.getRoles().contains(ruolo);
        }

        public List<RichiestaRuolo> getMieRichiesteRuolo(Users currentUser){
        return richiestaRepository.findRichiesteRuoloByUser(currentUser);
        }

        @Override
        public void processaRichiesta(Integer richiestaId, boolean approvato) {
            if (richiestaRepository.existsRichiestaRuoloById(richiestaId)) {
                RichiestaRuolo richiesta = (RichiestaRuolo) richiestaRepository.findById(richiestaId).get();
                if (approvato) {
                    userService.aggiungiRuolo(richiesta.getUser().getId(), richiesta.getRuoloRichiesto());
                    richiesta.setStato(StatoContenuto.ACCETTATA);
                } else {
                    richiesta.setStato(StatoContenuto.RIFIUTATA);
                }
                richiestaRepository.save(richiesta);
            } else throw new RuntimeException("Richiesta non trovata");
        }
    }

