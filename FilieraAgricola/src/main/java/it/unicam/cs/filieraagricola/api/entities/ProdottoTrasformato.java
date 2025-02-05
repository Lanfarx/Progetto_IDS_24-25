package it.unicam.cs.filieraagricola.api.entities;


import jakarta.persistence.*;import jakarta.persistence.Id;

@Entity
@DiscriminatorValue("TRASFORMATO")
public class ProdottoTrasformato extends Prodotto {

    //TODO vedere come gestire collegamento fasi della produzione con i produttori locali
    //TODO aggiungere variabile trasformatore

    private String processoTrasformazione;
    //Gestito i prodotti in modo che ogni prodotto trasfromato equivale ad uno ed un solo prodotto
    @ManyToOne
    @JoinColumn(name = "prodotto_base_id", nullable = false)
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
}
