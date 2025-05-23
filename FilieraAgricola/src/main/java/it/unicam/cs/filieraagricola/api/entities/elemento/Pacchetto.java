package it.unicam.cs.filieraagricola.api.entities.elemento;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
@Entity
public class Pacchetto extends Elemento{
    @ManyToMany
    @JoinTable(
            name = "Rpacchetto", // Nome della tabella di associazione
            joinColumns = @JoinColumn(name = "pacchetto_id"), // Chiave esterna per il Pacchetto
            inverseJoinColumns = @JoinColumn(name = "prodotto_id") // Chiave esterna per il Prodotto
    )
    private Set<Prodotto> prodottiSet = new HashSet<Prodotto>();

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

    @Override
    public void removeQuantita(int quantita){
        for (Prodotto prodotto : prodottiSet) {
            prodotto.removeQuantita(quantita);
        }
    }

    @Override
    public void aggiungiQuantita(int quantita) {
        for (Prodotto prodotto : prodottiSet) {
            prodotto.aggiungiQuantita(quantita);
        }
    }

}

