package it.unicam.cs.filieraagricola.api.model.utenti.operatori;

import it.unicam.cs.filieraagricola.api.model.contenuti.Contenuto;
import it.unicam.cs.filieraagricola.api.model.contenuti.iContenuto;
import it.unicam.cs.filieraagricola.api.model.utenti.iUtenteAutenticato;

public interface iOperatore {
    boolean caricaInformazioni(int id, String nome, String descrizione, double prezzo);
    boolean vendiContenuto(Contenuto contenuto);
}