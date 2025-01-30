package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Users;
import org.springframework.stereotype.Service;

@Service
public class VisitaEventoService {

    public boolean isOperatore(Users user) {
        return user.getRoles().contains(UserRole.PRODUTTORE) ||
                user.getRoles().contains(UserRole.TRASFORMATORE) ||
                user.getRoles().contains(UserRole.DISTRIBUTORE_DI_TIPICITA);
    }
}
