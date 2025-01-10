package it.unicam.cs.filieraagricola.api.model.utenti;

import it.unicam.cs.filieraagricola.api.model.contenuti.Contenuto;
import it.unicam.cs.filieraagricola.api.model.eventi.Visita;

import java.util.List;

public interface iAcquirente {

    boolean acquistaProdotto (Contenuto prodotto);

    List<Contenuto> getStoricoAcquisti();

    boolean prenotaEvento(Visita evento);

    List<Visita> getStoricoVisita();
}