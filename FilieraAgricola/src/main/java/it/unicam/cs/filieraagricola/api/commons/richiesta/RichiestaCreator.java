package it.unicam.cs.filieraagricola.api.commons.richiesta;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;

public interface RichiestaCreator {
    Richiesta creaRichiesta(Users user, Object valore);
}