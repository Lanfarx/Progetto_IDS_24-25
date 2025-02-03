package it.unicam.cs.filieraagricola.api.entities.richieste;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("RUOLO")
public class RichiestaRuolo extends Richiesta {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole ruoloRichiesto;

    public UserRole getRuoloRichiesto() { return ruoloRichiesto; }
    public void setRuoloRichiesto(UserRole ruoloRichiesto) { this.ruoloRichiesto = ruoloRichiesto; }
}