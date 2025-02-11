package it.unicam.cs.filieraagricola.api.entities.richieste;

import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@DiscriminatorValue("VALIDAZIONE")
public class RichiestaValidazione extends Richiesta {

    @OneToOne
    @JoinColumn(name = "elemento_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Elemento elemento;

    public Elemento getElemento() { return elemento; }
    public void setElemento(Elemento elemento) { this.elemento = elemento; }
}
