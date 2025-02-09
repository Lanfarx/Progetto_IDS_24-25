package it.unicam.cs.filieraagricola.api.entities.carrello;

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

    public ElementoCarrello(Elemento elemento, int quantita) {
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
        this.prezzoTotale = elemento.getPrezzo() * quantita;
    }
}
