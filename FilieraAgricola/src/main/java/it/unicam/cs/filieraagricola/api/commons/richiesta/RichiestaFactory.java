package it.unicam.cs.filieraagricola.api.commons.richiesta;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;

public class RichiestaFactory {
    public static Richiesta creaRichiesta(TipoRichiesta tipo, Users user, Object valore) {
        switch (tipo) {
            case RUOLO:
                if (valore instanceof UserRole) {
                    RichiestaRuolo richiestaRuolo = new RichiestaRuolo();
                    richiestaRuolo.setUser(user);
                    richiestaRuolo.setRuoloRichiesto((UserRole) valore);
                    richiestaRuolo.setStato(StatoContenuto.ATTESA);
                    return richiestaRuolo;
                } else {
                    throw new IllegalArgumentException("Valore non valido per la richiesta di tipo RUOLO");
                }
            case ELIMINAZIONE:
                if (valore instanceof String) {
                    RichiestaEliminazione richiestaEliminazione = new RichiestaEliminazione();
                    richiestaEliminazione.setUser(user);
                    richiestaEliminazione.setMotivazione((String) valore);
                    richiestaEliminazione.setStato(StatoContenuto.ATTESA);
                    return richiestaEliminazione;
                } else {
                    throw new IllegalArgumentException("Valore non valido per la richiesta di tipo ELIMINAZIONE");
                }
            case VALIDAZIONE:
                if (valore instanceof Elemento) {
                    RichiestaValidazione richiestaValidazione = new RichiestaValidazione();
                    richiestaValidazione.setUser(user);
                    richiestaValidazione.setElemento((Elemento) valore);
                    richiestaValidazione.setStato(StatoContenuto.ATTESA);
                    return richiestaValidazione;
                } else {
                    throw new IllegalArgumentException("Valore non valido per la richiesta di tipo VALIDAZIONE");
                }
            default:
                throw new IllegalArgumentException("Tipo di richiesta non valido");
        }
    }
}