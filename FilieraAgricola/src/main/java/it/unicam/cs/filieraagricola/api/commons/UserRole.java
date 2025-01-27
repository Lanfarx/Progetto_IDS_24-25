package it.unicam.cs.filieraagricola.api.commons;

public enum UserRole {
    PRODUTTORE,
    TRASFORMATORE,
    DISTRIBUTORE_DI_TIPICITA,
    CURATORE,
    ANIMATORE_DELLA_FILIERA,
    ACQUIRENTE,
    GESTORE_DELLA_PIATTAFORMA;

    public String toString(UserRole role) {
        return role.toString();
    }
}