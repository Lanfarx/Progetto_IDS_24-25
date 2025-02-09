package it.unicam.cs.filieraagricola.api.services.carrello;

import org.springframework.stereotype.Service;

@Service
public class PaymentManager {
    public boolean verificaPagamento(String cartaDiCredito) {
        return cartaDiCredito != null && cartaDiCredito.matches("\\d{12}");
    }
}
