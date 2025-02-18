package it.unicam.cs.filieraagricola.api.commons.richiesta;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import org.springframework.stereotype.Component;

@Component
public class RichiestaValidazioneFactory implements RichiestaCreator {

    @Override
    public Richiesta creaRichiesta(Users user, Object valore) {
        if (!(valore instanceof Elemento)) {
            throw new IllegalArgumentException("Valore non valido per la richiesta di tipo VALIDAZIONE");
        }
        RichiestaValidazione richiesta = new RichiestaValidazione();
        richiesta.setUser(user);
        richiesta.setElemento((Elemento) valore);
        richiesta.setStato(StatoContenuto.ATTESA);
        return richiesta;
    }
}