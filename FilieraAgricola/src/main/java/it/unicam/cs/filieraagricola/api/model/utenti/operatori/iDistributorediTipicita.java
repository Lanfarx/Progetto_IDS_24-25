package it.unicam.cs.filieraagricola.api.model.utenti.operatori;

import it.unicam.cs.filieraagricola.api.model.contenuti.Contenuto;
import it.unicam.cs.filieraagricola.api.model.contenuti.Pacchetto;
import it.unicam.cs.filieraagricola.api.model.contenuti.Prodotto;
import it.unicam.cs.filieraagricola.api.model.utenti.iUtenteAutenticato;

import java.util.List;

public interface iDistributorediTipicita extends iOperatore {

    boolean caricaInformazioni(int id, String nome, String descrizione, double prezzo, List<Prodotto> prodotti);
    boolean vendiContenuto(Pacchetto pacchetto);
}
