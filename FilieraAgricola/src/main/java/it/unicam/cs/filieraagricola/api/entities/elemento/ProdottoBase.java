package it.unicam.cs.filieraagricola.api.entities.elemento;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("BASE")
public class ProdottoBase extends Prodotto {

    private String metodiDiColtivazione;
    public String getMetodiDiColtivazione() {
        return metodiDiColtivazione;
    }

    public void setMetodiDiColtivazione(String metodiDiColtivazione) {
        this.metodiDiColtivazione = metodiDiColtivazione;
    }
}