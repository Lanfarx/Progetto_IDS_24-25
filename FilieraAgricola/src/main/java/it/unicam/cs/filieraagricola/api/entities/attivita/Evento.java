package it.unicam.cs.filieraagricola.api.entities.attivita;

import it.unicam.cs.filieraagricola.api.entities.Users;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Evento")
public class Evento extends Visita {

    // Lista di invitati (utenti con ruoli specifici)
    @ManyToMany
    @JoinTable(
            name = "evento_invitati",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Users> invitati = new HashSet<>();; // Solo utenti con ruoli specifici

    public Set<Users> getInvitati() {
        return invitati;
    }

    public void setInvitati(Set<Users> invitati) {
        this.invitati = invitati;
    }
}