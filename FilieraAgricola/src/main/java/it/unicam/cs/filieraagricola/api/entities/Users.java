package it.unicam.cs.filieraagricola.api.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Richiesta> richieste;

    @JsonIgnore
    @OneToMany(mappedBy = "operatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pacchetto> pacchettiCreati = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "operatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdottoTrasformato> prodottiTrasformatiCreati = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "operatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdottoBase> prodottiBaseCreati = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "organizzatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visita> attivitaOrganizzate = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "prenotazioni", cascade = CascadeType.ALL)
    private Set<Visita> attivitaPrenotate = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public List<Richiesta> getRichieste() {
        return richieste;
    }

    public void setRichieste(List<Richiesta> richieste) {
        this.richieste = richieste;
    }

    public List<Visita> getAttivitaOrganizzate() {
        return attivitaOrganizzate;
    }

    public void setAttivitaOrganizzate(List<Visita> attivitaOrganizzate) {
        this.attivitaOrganizzate = attivitaOrganizzate;
    }

    public Set<Visita> getAttivitaPrenotate() {
        return attivitaPrenotate;
    }

    public void setAttivitaPrenotate(Set<Visita> attivitaPrenotate) {
        this.attivitaPrenotate = attivitaPrenotate;
    }

    public List<Pacchetto> getPacchettiCreati() {
        return pacchettiCreati;
    }

    public void setPacchettiCreati(List<Pacchetto> pacchettiCreati) {
        this.pacchettiCreati = pacchettiCreati;
    }

    public List<ProdottoTrasformato> getProdottiTrasformatiCreati() {
        return prodottiTrasformatiCreati;
    }

    public void setProdottiTrasformatiCreati(List<ProdottoTrasformato> prodottiTrasformatiCreati) {
        this.prodottiTrasformatiCreati = prodottiTrasformatiCreati;
    }

    public List<ProdottoBase> getProdottiBaseCreati() {
        return prodottiBaseCreati;
    }

    public void setProdottiBaseCreati(List<ProdottoBase> prodottiBaseCreati) {
        this.prodottiBaseCreati = prodottiBaseCreati;
    }
}