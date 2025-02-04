package it.unicam.cs.filieraagricola.api.services.gestore.richieste;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;

import java.util.List;
import java.util.Optional;

public interface RichiestaService<T extends Richiesta> {

    void aggiungiRichiesta(Integer userId, Object valore);

    boolean existsRichiesta(Integer id);

    boolean existsSameRichiesta(Users user, Object valore);

    Optional<T> getRichiesta(Integer id);

    List<T> getRichiesteInAttesa();

    void processaRichiesta(Integer richiestaId, boolean approvato);
}
