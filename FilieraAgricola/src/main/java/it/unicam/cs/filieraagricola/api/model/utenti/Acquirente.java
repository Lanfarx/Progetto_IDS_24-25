package it.unicam.cs.filieraagricola.api.model.utenti;

import it.unicam.cs.filieraagricola.api.model.contenuti.Contenuto;
import it.unicam.cs.filieraagricola.api.model.eventi.Visita;

import java.util.List;

public class Acquirente implements iAcquirente{
    @Override
    public boolean acquistaProdotto(Contenuto prodotto) {
        return false;
    }

    @Override
    public List<Contenuto> getStoricoAcquisti() {
        return List.of();
    }

    @Override
    public boolean prenotaEvento(Visita evento) {
        return false;
    }

    @Override
    public List<Visita> getStoricoVisita() {
        return List.of();
    }
}
