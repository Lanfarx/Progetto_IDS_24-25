package it.unicam.cs.filieraagricola.api.entities.richieste;

import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("VALIDAZIONE")
public class RichiestaValidazione extends Richiesta {

    @OneToOne
    @JoinColumn(name = "elemento_id")
    private Elemento elemento;

    public Elemento getElemento() { return elemento; }
    public void setElemento(Elemento elemento) { this.elemento = elemento; }
}
