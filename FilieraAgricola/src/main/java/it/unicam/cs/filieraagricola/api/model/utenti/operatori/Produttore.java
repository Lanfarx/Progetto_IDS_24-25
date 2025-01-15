package it.unicam.cs.filieraagricola.api.model.utenti.operatori;

import it.unicam.cs.filieraagricola.api.model.contenuti.ProdottoBase;

public class Produttore implements iProduttore{
    @Override
    public boolean caricaInformazione(int id, String nome, String descrizione, double prezzo, String metodoColtivazione, String certificazione) {
        return false;
    }

    @Override
    public boolean vendiContenuto(ProdottoBase prodotto) {
        return false;
    }
}
