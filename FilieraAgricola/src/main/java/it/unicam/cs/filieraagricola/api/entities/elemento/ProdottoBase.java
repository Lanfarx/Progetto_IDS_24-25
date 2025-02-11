package it.unicam.cs.filieraagricola.api.entities.elemento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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