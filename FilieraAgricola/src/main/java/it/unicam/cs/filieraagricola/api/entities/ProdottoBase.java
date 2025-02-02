package it.unicam.cs.filieraagricola.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("BASE")
public class ProdottoBase extends Prodotto {

    //TODO aggiungere variabile produttore

    private String metodiDiColtivazione;

    public String getMetodiDiColtivazione() {
        return metodiDiColtivazione;
    }

    public void setMetodiDiColtivazione(String metodiDiColtivazione) {
        this.metodiDiColtivazione = metodiDiColtivazione;
    }
}