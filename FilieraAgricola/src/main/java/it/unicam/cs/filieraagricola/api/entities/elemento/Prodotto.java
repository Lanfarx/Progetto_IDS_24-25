package it.unicam.cs.filieraagricola.api.entities.elemento;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_prodotto", discriminatorType = DiscriminatorType.STRING)
public abstract class Prodotto extends Elemento{


    //TODO aggiungere variabile produttore Produttore produttoreOriginale
    private String certificazioni;
    private int quantita;

    @ManyToMany(mappedBy = "prodottiSet") @JsonBackReference
    private Set<Pacchetto> pacchettiSet = new HashSet<>();


    @PreRemove
    private void preRemove() {
        for (Pacchetto pacchetto : new HashSet<>(pacchettiSet)) {
            pacchetto.removeProdotto(this);
        }
        pacchettiSet.clear();
    }
    public String getCertificazioni() {
        return certificazioni;
    }
    public void setCertificazioni(String certificazioni) {
        this.certificazioni = certificazioni;
    }
    @Override
    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
    public void addQuantita(int quantita) { this.quantita = this.quantita + quantita; }
    public void removeQuantita(int quantita) { this.quantita = Math.min(this.quantita - quantita, 0); }

}
