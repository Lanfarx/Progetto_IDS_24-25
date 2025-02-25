package it.unicam.cs.filieraagricola.api.entities.richieste;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;

public interface iRichiesta {
    int getId();
    void setId(int id);

    Users getUser();
    void setUser(Users user);

    StatoContenuto getStato();
    void setStato(StatoContenuto stato);
}
