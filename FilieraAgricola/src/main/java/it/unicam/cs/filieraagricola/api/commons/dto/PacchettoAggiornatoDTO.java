package it.unicam.cs.filieraagricola.api.commons.dto;

import java.util.Set;

public class PacchettoAggiornatoDTO {
    private int id;
    private String nome;
    private String descrizione;
    private double prezzo;
    private Set<Integer> prodottiIds;

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

    public Set<Integer> getProdottiIds() {
        return prodottiIds;
    }

    public void setProdottiIds(Set<Integer> prodottiIds) {
        this.prodottiIds = prodottiIds;
    }
}
