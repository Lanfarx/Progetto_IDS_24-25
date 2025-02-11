package it.unicam.cs.filieraagricola.api.services.carrello;

import org.springframework.stereotype.Component;

@Component
public class CartaDiCreditoPayment implements PaymentStrategy {
    @Override
    public boolean pagamento(String numeroCarta) {
        return numeroCarta != null && numeroCarta.matches("\\d{12}");
    }
}