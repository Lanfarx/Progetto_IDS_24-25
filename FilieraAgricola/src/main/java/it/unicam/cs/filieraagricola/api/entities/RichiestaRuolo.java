package it.unicam.cs.filieraagricola.api.entities;

import it.unicam.cs.filieraagricola.api.commons.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.commons.UserRole;
import jakarta.persistence.*;

@Entity
public class RichiestaRuolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole ruoloRichiesto;

    @Enumerated(EnumType.STRING)
    private StatoRichiesta stato = StatoRichiesta.ATTESA;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public UserRole getRuoloRichiesto() {
        return ruoloRichiesto;
    }

    public void setRuoloRichiesto(UserRole ruoloRichiesto) {
        this.ruoloRichiesto = ruoloRichiesto;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }
}