package it.unicam.cs.filieraagricola.api.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Pacchetto extends Elemento{

    private String descrizione; //TODO vedere se toglierla

    @ManyToMany (cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "Rpacchetto", // Nome della tabella di associazione
            joinColumns = @JoinColumn(name = "pacchetto_id"), // Chiave esterna per il Pacchetto
            inverseJoinColumns = @JoinColumn(name = "prodotto_id") // Chiave esterna per il Prodotto
    )
    private Set<Prodotto> prodottiSet = new HashSet<Prodotto>();

    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public Set<Prodotto> getProdottiSet() {
        return prodottiSet;
    }
    public void setProdottiSet(Set<Prodotto> prodottiSet) {
        this.prodottiSet = prodottiSet;
    }
    public void addProdotto(Prodotto prodotto) {
        prodottiSet.add(prodotto);
    }
    public void removeProdotto(Prodotto prodotto) { prodottiSet.remove(prodotto); }


    public boolean disponibile(){ return getQuantita()>0; }

    @Override
    public int getQuantita() {
        int quantita = Integer.MAX_VALUE;
        for (Prodotto prodotto : prodottiSet) {
            if(prodotto.getQuantita() < quantita) {
                quantita = prodotto.getQuantita();
            }
        }
        return quantita;
    }

    public void removeQuantita(int quantita){
        for (Prodotto prodotto : prodottiSet) {
            prodotto.removeQuantita(quantita);
        }
    }

}
