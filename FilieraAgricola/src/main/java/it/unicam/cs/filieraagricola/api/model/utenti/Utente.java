package it.unicam.cs.filieraagricola.api.model.utenti;

public class Utente implements iUtente{

    @Override
    public boolean login(String email, String password) {
        return false;
    }
}
