package it.unicam.cs.filieraagricola.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ProdottoBase {
    @Id
    private int id;
    private String nome;
    private String certificazioni;
    private String metodiDiColtivazione;
    private double prezzo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCertificazioni() {
        return certificazioni;
    }

    public void setCertificazioni(String certificazioni) {
        this.certificazioni = certificazioni;
    }

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

    public String getMetodiDiColtivazione() {
        return metodiDiColtivazione;
    }

    public void setMetodiDiColtivazione(String metodiDiColtivazione) {
        this.metodiDiColtivazione = metodiDiColtivazione;
    }

    public ProdottoBase() {
    }

}