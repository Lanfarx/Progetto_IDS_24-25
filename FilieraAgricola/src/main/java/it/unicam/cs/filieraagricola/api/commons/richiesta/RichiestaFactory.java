package it.unicam.cs.filieraagricola.api.commons.richiesta;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.entities.Users;

public class RichiestaFactory {
    public static Richiesta creaRichiesta(String tipo, Users user, Object valore) {
        switch (tipo) {
            case "RUOLO":
                if (valore instanceof UserRole) {
                }
                RichiestaRuolo richiestaRuolo = new RichiestaRuolo();
                richiestaRuolo.setUser(user);
                richiestaRuolo.setRuoloRichiesto((UserRole) valore);
                return richiestaRuolo;
            case "ELIMINAZIONE":
                RichiestaEliminazione richiestaEliminazione = new RichiestaEliminazione();
                richiestaEliminazione.setUser(user);
                richiestaEliminazione.setMotivazione((String) valore);
                return richiestaEliminazione;
            default:
                throw new IllegalArgumentException("Tipo di richiesta non valido");
        }
    }
}