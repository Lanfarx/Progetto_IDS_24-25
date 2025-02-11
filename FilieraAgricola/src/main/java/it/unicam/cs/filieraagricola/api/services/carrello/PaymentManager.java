package it.unicam.cs.filieraagricola.api.services.carrello;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentManager {
    private final Map<String, PaymentStrategy> strategies;

    public PaymentManager(Map<String, PaymentStrategy> strategies) {
        this.strategies = strategies;
    }

    public boolean effettuaPagamento(String metodo, String datiPagamento) {
        PaymentStrategy strategy = strategies.get(metodo);
        if (strategy != null) {
            return strategy.pagamento(datiPagamento);
        }
        return false;
    }
}