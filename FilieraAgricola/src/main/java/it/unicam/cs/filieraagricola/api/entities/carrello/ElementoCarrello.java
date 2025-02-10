package it.unicam.cs.filieraagricola.api.entities.carrello;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import jakarta.persistence.*;

@Entity
public class ElementoCarrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "elemento_id")
    private Elemento elemento;

    private int quantita;

    private double prezzoTotale;

    @ManyToOne @JsonIgnore
    @JoinColumn(name = "carrello_id")
    private Carrello carrello;

    @ManyToOne @JsonIgnore
    @JoinColumn(name = "ordine_id")
    private Ordine ordine;

    public ElementoCarrello(Carrello carrello, Elemento elemento, int quantita) {
        this.carrello = carrello;
        this.elemento = elemento;
        this.quantita = quantita;
        this.prezzoTotale = elemento.getPrezzo() * quantita;
    }

    public ElementoCarrello() {

    }

    public int getId(){
        return id;
    }

    private void setId(int id){
        this.id = id;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
        this.prezzoTotale = elemento.getPrezzo() * quantita;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public void rimuoviQuantita(int quantita) {
        this.quantita -= quantita;
        this.prezzoTotale = elemento.getPrezzo() * this.quantita;
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }
}
