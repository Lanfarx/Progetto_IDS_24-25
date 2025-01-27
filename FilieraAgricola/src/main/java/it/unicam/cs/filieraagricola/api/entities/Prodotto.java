package it.unicam.cs.filieraagricola.api.entities;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Puoi usare anche SINGLE_TABLE o TABLE_PER_CLASS
public abstract class Prodotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String certificazioni;
    private double prezzo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }


    public String getCertificazioni() {
        return certificazioni;
    }

    public void setCertificazioni(String certificazioni) {
        this.certificazioni = certificazioni;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
