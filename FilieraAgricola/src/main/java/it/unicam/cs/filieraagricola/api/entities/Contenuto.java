package it.unicam.cs.filieraagricola.api.entities;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoRichiesta;
import jakarta.persistence.*;


//TODO togliere variabili prezzo, nome e descrizioni già presenti in Elemento
@Entity
public class Contenuto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private StatoRichiesta verificato;


    @OneToOne
    @JoinColumn(name = "elemento_id") // Nome della colonna nella tabella Contenuto
    private Elemento elemento;

    public Elemento getElemento() { return elemento; }

    public void setElemento(Elemento elemento) { this.elemento = elemento; }

    public boolean isVerificato() { return verificato==StatoRichiesta.ACCETTATA; }

    public StatoRichiesta getVerificato() { return verificato; }

    public void setVerificato(StatoRichiesta verificato) { this.verificato = verificato; }

    public double getPrezzo() { return elemento.getPrezzo(); }

    public void setPrezzo(double prezzo) { this.elemento.setPrezzo(prezzo); }

    public String getDescrizione() { return elemento.getDescrizione(); }

    public void setDescrizione(String descrizione) { this.elemento.setDescrizione(descrizione); }

    public String getNome() { return elemento.getNome(); }

    public void setNome(String nome) { this.elemento.setNome(nome); }

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public int getQuantita() { return elemento.getQuantita(); }

    public boolean disponibile(){ return getQuantita()>0; }

}
