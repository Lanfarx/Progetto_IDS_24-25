package it.unicam.cs.filieraagricola.api.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_visita", discriminatorType = DiscriminatorType.STRING)
public class Visita implements Attivita{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titolo;

    private String descrizione;

    private LocalDate data;

    // Relazione con Users per le prenotazioni
    @ManyToMany
    @JoinTable(
            name = "visita_prenotazioni",
            joinColumns = @JoinColumn(name = "visita_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Users> prenotazioni = new HashSet<>();;

    private String luogo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Set<Users> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(Set<Users> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }
}