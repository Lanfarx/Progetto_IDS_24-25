package it.unicam.cs.filieraagricola.api.model.utenti.operatori;

import it.unicam.cs.filieraagricola.api.model.contenuti.ProdottoBase;
import it.unicam.cs.filieraagricola.api.model.utenti.iUtenteAutenticato;

public interface iProduttore {

    boolean caricaInformazione(int id, String nome, String descrizione, double prezzo, String metodoColtivazione, String certificazione);
    boolean vendiContenuto(ProdottoBase prodotto);
}
