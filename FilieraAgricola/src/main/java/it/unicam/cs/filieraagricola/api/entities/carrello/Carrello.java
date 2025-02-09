package it.unicam.cs.filieraagricola.api.entities.carrello;

import it.unicam.cs.filieraagricola.api.entities.Users;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users user;

    @OneToMany
    private List<ElementoCarrello> elementi;

    private double prezzoTotale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<ElementoCarrello> getElementi() {
        if(elementi == null) {
            elementi = new ArrayList<>();
        }
        return elementi;
    }

    public void setElementi(List<ElementoCarrello> elementi) {
        this.elementi = elementi;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }
}
