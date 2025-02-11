package it.unicam.cs.filieraagricola.api.entities.carrello;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unicam.cs.filieraagricola.api.entities.Users;
import jakarta.persistence.*;

import javax.swing.text.Element;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Users user;

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementoOrdine> elementi = new ArrayList<>();

    private double prezzoTotale;

    private LocalDateTime dataOrdine;

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

    public List<ElementoOrdine> getElementi() {
        return elementi;
    }

    public void setElementi(List<ElementoOrdine> elementi) {
        this.elementi = elementi;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public LocalDateTime getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(LocalDateTime dataOrdine) {
        this.dataOrdine = dataOrdine;
    }
}