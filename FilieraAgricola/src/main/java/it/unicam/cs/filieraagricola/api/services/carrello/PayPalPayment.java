package it.unicam.cs.filieraagricola.api.services.carrello;

import org.springframework.stereotype.Component;

@Component
public class PayPalPayment implements PaymentStrategy {
    @Override
    public boolean pagamento(String email) {
        return email != null && email.contains("@");
    }
}