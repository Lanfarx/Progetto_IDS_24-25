package it.unicam.cs.filieraagricola.api.services.carrello;

import org.springframework.stereotype.Component;

@Component
public class BonificoPayment implements PaymentStrategy {
    @Override
    public boolean pagamento(String iban) {
        return iban != null && iban.matches("[A-Z]{2}\\d{2}[A-Z0-9]{11,30}");
    }
}