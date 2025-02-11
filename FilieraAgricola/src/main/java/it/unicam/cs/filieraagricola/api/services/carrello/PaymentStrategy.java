package it.unicam.cs.filieraagricola.api.services.carrello;

public interface PaymentStrategy {
    boolean pagamento(String datiPagamento);
}