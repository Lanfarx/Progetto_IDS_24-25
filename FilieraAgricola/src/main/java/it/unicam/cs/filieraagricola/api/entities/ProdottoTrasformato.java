package it.unicam.cs.filieraagricola.api.entities;


import jakarta.persistence.*;import jakarta.persistence.Id;

@Entity
public class ProdottoTrasformato extends Prodotto {

    private String processoTrasformazione;

    //TODO aggiungere variabile produttore Produttore produttoreOriginale

    //Gestito i prodotti in modo che ogni prodotto trasfromato equivale ad uno ed un solo prodotto
    @ManyToOne
    @JoinColumn(name = "prodotto_base_id", nullable = false)  // Colonna di join
    private ProdottoBase prodottoBase;

    public ProdottoBase getProdottoBase() {
        return this.prodottoBase;
    }

    public void setProdottoBase(ProdottoBase prodottoBase) {
        this.prodottoBase = prodottoBase;
    }

    public String getProcessoTrasformazione() {
        return this.processoTrasformazione;
    }

    public void setProcessoTrasformazione(String processoTrasformazione) {
        this.processoTrasformazione = processoTrasformazione;
    }


    //Come gestire "la possibilità di collegare le fasi della produzione ai produttori locali"?


}
