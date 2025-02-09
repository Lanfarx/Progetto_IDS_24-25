package it.unicam.cs.filieraagricola.api.entities.elemento;


import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;
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


    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "operatore_id", nullable = false)
    private Users operatore;

    @Enumerated(EnumType.STRING)
    private StatoContenuto statorichiesta;

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria){ this.categoria = categoria; }

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
    public StatoContenuto getStatorichiesta(){ return statorichiesta; }
    public abstract void removeQuantita(int quantita);
    public abstract void aggiungiQuantita(int quantita);
    public void setStatorichiesta(StatoContenuto statorichiesta){ this.statorichiesta = statorichiesta; }
    public abstract int getQuantita();
    public Users getOperatore() {
        return operatore;
    }
    public void setOperatore(Users operatore) {
        this.operatore = operatore;
    }
}
