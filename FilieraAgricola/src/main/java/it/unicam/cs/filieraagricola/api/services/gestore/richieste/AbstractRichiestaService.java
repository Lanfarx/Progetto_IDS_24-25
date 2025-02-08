package it.unicam.cs.filieraagricola.api.services.gestore.richieste;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.repository.RichiestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class AbstractRichiestaService<T extends Richiesta> {

    @Autowired
    protected RichiestaRepository richiestaRepository; // Proteggo per l'accesso dalle sottoclassi

    public List<Richiesta> getRichiesteByUser(Users user) {
        return richiestaRepository.findByUser(user);
    }

    public abstract void aggiungiRichiesta(Integer userId, Object valore);
    public abstract boolean existsRichiesta(Integer id);
    public abstract boolean existsSameRichiesta(Users user, Object valore);
    public abstract Optional<T> getRichiesta(Integer id);
    public abstract List<T> getRichiesteInAttesa();
    public abstract void processaRichiesta(Integer richiestaId, boolean approvato);
}
