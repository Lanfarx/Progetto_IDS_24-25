package it.unicam.cs.filieraagricola.api.model.utenti.operatori;

import it.unicam.cs.filieraagricola.api.model.contenuti.Contenuto;
import it.unicam.cs.filieraagricola.api.model.contenuti.Pacchetto;
import it.unicam.cs.filieraagricola.api.model.contenuti.Prodotto;

import java.util.List;

public class DistributorediTipicita implements iDistributorediTipicita{

    @Override
    public boolean caricaInformazioni(int id, String nome, String descrizione, double prezzo, List<Prodotto> prodotti) {
        return false;
    }

    @Override
    public boolean vendiContenuto(Pacchetto pacchetto) {
        return false;
    }

    @Override
    public boolean caricaInformazioni(int id, String nome, String descrizione, double prezzo) {
        return false;
    }

    @Override
    public boolean vendiContenuto(Contenuto contenuto) {
        return false;
    }
}

