package it.unicam.cs.filieraagricola.api.commons.richiesta;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RichiestaFactory {

    private final Map<TipoRichiesta, RichiestaCreator> creators = new HashMap<>();

    @Autowired
    public RichiestaFactory(
            RichiestaRuoloFactory richiestaRuoloFactory,
            RichiestaEliminazioneFactory richiestaEliminazioneFactory,
            RichiestaValidazioneFactory richiestaValidazioneFactory
    ) {
        creators.put(TipoRichiesta.RUOLO, richiestaRuoloFactory);
        creators.put(TipoRichiesta.ELIMINAZIONE, richiestaEliminazioneFactory);
        creators.put(TipoRichiesta.VALIDAZIONE, richiestaValidazioneFactory);
    }

    public Richiesta creaRichiesta(TipoRichiesta tipo, Users user, Object valore) {
        RichiestaCreator creator = creators.get(tipo);
        if (creator == null) {
            throw new IllegalArgumentException("Tipo di richiesta non valido: " + tipo);
        }
        return creator.creaRichiesta(user, valore);
    }
}