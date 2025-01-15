package it.unicam.cs.filieraagricola.api.model.utenti;

public class UtenteAutenticato implements iUtenteAutenticato {
    @Override
    public boolean logout() {
        return false;
    }
}
