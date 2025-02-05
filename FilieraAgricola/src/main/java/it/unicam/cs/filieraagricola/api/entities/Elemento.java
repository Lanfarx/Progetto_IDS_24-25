package it.unicam.cs.filieraagricola.api.entities;


import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoRichiesta;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Elemento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String descrizione;
    private double prezzo;

<<<<<<< Updated upstream
    @ManyToOne
    @JoinColumn(name = "operatore_id", nullable = false)
    private Users operatore;
=======
    private StatoRichiesta statorichiesta;
>>>>>>> Stashed changes

    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public double getPrezzo() {
        return prezzo;
    }
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public StatoRichiesta getStatorichiesta(){ return statorichiesta; }
    public void setStatorichiesta(StatoRichiesta statorichiesta){ this.statorichiesta = statorichiesta; }

    public abstract int getQuantita();

    public Users getOperatore() {
        return operatore;
    }

    public void setOperatore(Users operatore) {
        this.operatore = operatore;
    }
}
