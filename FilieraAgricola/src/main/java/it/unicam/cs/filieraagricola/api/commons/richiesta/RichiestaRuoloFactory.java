package it.unicam.cs.filieraagricola.api.commons.richiesta;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import org.springframework.stereotype.Component;

@Component
public class RichiestaRuoloFactory implements RichiestaCreator {

    @Override
    public Richiesta creaRichiesta(Users user, Object valore) {
        if (!(valore instanceof UserRole)) {
            throw new IllegalArgumentException("Valore non valido per la richiesta di tipo RUOLO");
        }
        RichiestaRuolo richiesta = new RichiestaRuolo();
        richiesta.setUser(user);
        richiesta.setRuoloRichiesto((UserRole) valore);
        richiesta.setStato(StatoContenuto.ATTESA);
        return richiesta;
    }
}