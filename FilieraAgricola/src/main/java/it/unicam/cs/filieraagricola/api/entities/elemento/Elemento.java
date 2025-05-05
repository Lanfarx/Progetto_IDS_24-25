package it.unicam.cs.filieraagricola.api.entities.elemento;


import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Elemento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String descrizione;
    private double prezzo;

    private String categoria;

    @ManyToOne
    @JoinColumn(name = "operatore_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users operatore;

    @Enumerated(EnumType.STRING)
    private StatoContenuto statorichiesta;

    public Elemento() {
        this.statorichiesta = StatoContenuto.ATTESA;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria){ this.categoria = categoria; }
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
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public StatoContenuto getStatorichiesta(){ return statorichiesta; }
    public abstract void removeQuantita(int quantita);
    public abstract void aggiungiQuantita(int quantita);
    public void setStatorichiesta(StatoContenuto statorichiesta){ this.statorichiesta = statorichiesta; }
    public abstract int getQuantita();
    public Users getOperatore() {
        return operatore;
    }
    public void setOperatore(Users operatore) {
        this.operatore = operatore;
    }
}
