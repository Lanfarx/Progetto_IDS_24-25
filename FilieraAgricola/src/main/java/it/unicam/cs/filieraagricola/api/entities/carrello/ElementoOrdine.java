package it.unicam.cs.filieraagricola.api.entities.carrello;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class ElementoOrdine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nomeProdotto;
    private String descrizione;
    private double prezzoProdotto;
    private int quantita;

    @ManyToOne @JsonIgnore
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine ordine;

    public ElementoOrdine(int quantita, double prezzoProdotto, String descrizione, String nomeProdotto) {
        this.quantita = quantita;
        this.prezzoProdotto = prezzoProdotto;
        this.descrizione = descrizione;
        this.nomeProdotto = nomeProdotto;
    }
 
    public ElementoOrdine() {

    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getPrezzoProdotto() {
        return prezzoProdotto;
    }

    public void setPrezzoProdotto(double prezzoProdotto) {
        this.prezzoProdotto = prezzoProdotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
