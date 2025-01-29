package it.unicam.cs.filieraagricola.api.entities;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Puoi usare anche SINGLE_TABLE o TABLE_PER_CLASS
public abstract class Prodotto extends Elemento{

    private String certificazioni;

    public String getCertificazioni() {
        return certificazioni;
    }

    public void setCertificazioni(String certificazioni) {
        this.certificazioni = certificazioni;
    }

}
