package it.unicam.cs.filieraagricola.api.services.gestore.richieste;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.commons.richiesta.TipoRichiesta;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.repository.RichiestaRepository;
import it.unicam.cs.filieraagricola.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static it.unicam.cs.filieraagricola.api.commons.richiesta.RichiestaFactory.creaRichiesta;

@Service
    public class RichiestaEliminazioneService extends AbstractRichiestaService<RichiestaEliminazione> {

        @Autowired
        private RichiestaRepository richiestaRepository;

        @Autowired
        private UserService userService;

        @Override
        public void aggiungiRichiesta(Integer userId, Object motivazione) {
            Users user = userService.getUserById(userId).get();
            richiestaRepository.save(creaRichiesta(TipoRichiesta.ELIMINAZIONE, user, motivazione));
        }

        @Override
        public boolean existsRichiesta(Integer id){
            return richiestaRepository.existsRichiestaEliminazioneById(id);
        }

        @Override
        public boolean existsSameRichiesta(Users user, Object motivazione){
            return richiestaRepository.existsRichiestaEliminazioneByUserAndMotivazione(user, (String) motivazione);
        }

        @Override
        public Optional<RichiestaEliminazione> getRichiesta(Integer id){
            return richiestaRepository.findRichiestaEliminazioneById(id);
        }

        @Override
        public List<RichiestaEliminazione> getRichiesteInAttesa() {
            return richiestaRepository.findRichiestaEliminazioneByStato(StatoContenuto.ATTESA);
        }

        public List<RichiestaEliminazione> getMieRichiesteEliminazione(Users currentUser){
            return richiestaRepository.findRichiesteEliminazioneByUser(currentUser);
        }

        @Override
        public void processaRichiesta(Integer richiestaId, boolean approvato) {
            if (richiestaRepository.existsRichiestaEliminazioneById(richiestaId)) {
                RichiestaEliminazione richiesta = (RichiestaEliminazione) richiestaRepository.findById(richiestaId).get();
                if (approvato) {
                    userService.delete(richiesta.getUser());
                } else {
                    richiesta.setStato(StatoContenuto.RIFIUTATA);
                    richiestaRepository.save(richiesta);
                }
            } else throw new RuntimeException("Richiesta non trovata");
        }
    }