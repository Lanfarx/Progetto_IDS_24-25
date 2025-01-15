package it.unicam.cs.filieraagricola.api.model.utenti;

import it.unicam.cs.filieraagricola.api.model.contenuti.Contenuto;

public class Curatore implements iCuratore {
    @Override
    public boolean verificaContenuto(Contenuto contenuto) {
        return false;
    }
}
