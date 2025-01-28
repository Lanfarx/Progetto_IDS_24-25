package it.unicam.cs.filieraagricola.api.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Pacchetto implements Elemento{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String descrizione;
    private double prezzo;

    @ManyToMany
    @JoinTable(
            name = "Rpacchetto", // Nome della tabella di associazione
            joinColumns = @JoinColumn(name = "pacchetto_id"), // Chiave esterna per il Pacchetto
            inverseJoinColumns = @JoinColumn(name = "prodotto_id") // Chiave esterna per il Prodotto
    )
    private Set<Prodotto> prodottiSet = new HashSet<Prodotto>();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public double getPrezzo() {
        return prezzo;
    }
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
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

}
