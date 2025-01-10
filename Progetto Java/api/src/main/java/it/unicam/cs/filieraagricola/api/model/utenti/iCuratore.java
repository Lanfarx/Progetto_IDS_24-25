package it.unicam.cs.filieraagricola.api.model.utenti;

import it.unicam.cs.filieraagricola.api.model.contenuti.Contenuto;

public interface iCuratore {

    boolean verificaContenuto(Contenuto contenuto);
}
