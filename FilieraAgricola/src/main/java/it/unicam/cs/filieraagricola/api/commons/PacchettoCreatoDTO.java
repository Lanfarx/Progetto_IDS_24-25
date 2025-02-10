package it.unicam.cs.filieraagricola.api.commons;

import java.util.Set;

public class PacchettoCreatoDTO {
    private String nome;
    private String descrizione;
    private double prezzo;
    private Set<Integer> prodottiIds;

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
