package it.unicam.cs.filieraagricola.api.entities.richieste;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ELIMINAZIONE")
public class RichiestaEliminazione extends Richiesta {
    @Column(length = 500)
    private String motivazione;

    public String getMotivazione() { return motivazione; }
    public void setMotivazione(String motivazione) { this.motivazione = motivazione; }
}