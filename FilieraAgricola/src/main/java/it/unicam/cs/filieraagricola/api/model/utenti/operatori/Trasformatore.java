package it.unicam.cs.filieraagricola.api.model.utenti.operatori;

import it.unicam.cs.filieraagricola.api.model.contenuti.ProdottoBase;
import it.unicam.cs.filieraagricola.api.model.contenuti.ProdottoTrasformato;

import java.util.List;

public class Trasformatore implements iTrasformatore{
    @Override
    public boolean caricaInformazioni(int id, String nome, String descrizione, double prezzo, ProdottoBase prodotto, String processiTrasformazione) {
        return false;
    }

    @Override
    public boolean collegaProduttori(ProdottoTrasformato prodotto, List<Produttore> produttori) {
        return false;
    }

    @Override
    public boolean vendiContenuto(ProdottoTrasformato prodotto) {
        return false;
    }
}
