package it.unicam.cs.filieraagricola.api.commons.richiesta;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import org.springframework.stereotype.Component;

@Component
public class RichiestaEliminazioneFactory implements RichiestaCreator {

    @Override
    public Richiesta creaRichiesta(Users user, Object valore) {
        if (!(valore instanceof String)) {
            throw new IllegalArgumentException("Valore non valido per la richiesta di tipo ELIMINAZIONE");
        }
        RichiestaEliminazione richiesta = new RichiestaEliminazione();
        richiesta.setUser(user);
        richiesta.setMotivazione((String) valore);
        richiesta.setStato(StatoContenuto.ATTESA);
        return richiesta;
    }
}