package it.unicam.cs.filieraagricola.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Tabelle separate con join
public class ProdottoBase extends Prodotto {

    private String metodiDiColtivazione;

    public String getMetodiDiColtivazione() {
        return metodiDiColtivazione;
    }

    public void setMetodiDiColtivazione(String metodiDiColtivazione) {
        this.metodiDiColtivazione = metodiDiColtivazione;
    }


    public ProdottoBase() {
    }

}