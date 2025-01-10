package it.unicam.cs.filieraagricola.api.model.utenti.operatori;

import it.unicam.cs.filieraagricola.api.model.contenuti.Prodotto;
import it.unicam.cs.filieraagricola.api.model.contenuti.ProdottoBase;
import it.unicam.cs.filieraagricola.api.model.contenuti.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.model.utenti.iUtenteAutenticato;

import java.util.List;

public interface iTrasformatore {

    boolean caricaInformazioni(int id, String nome, String descrizione, double prezzo, ProdottoBase prodotto, String processiTrasformazione);
    boolean collegaProduttori(ProdottoTrasformato prodotto, List<Produttore> produttori);
    boolean vendiContenuto(ProdottoTrasformato prodotto);
}
