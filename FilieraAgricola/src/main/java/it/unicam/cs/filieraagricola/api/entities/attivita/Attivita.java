package it.unicam.cs.filieraagricola.api.entities.attivita;

import it.unicam.cs.filieraagricola.api.entities.Users;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public interface Attivita {
    String getTitolo();
    void setTitolo(String titolo);
    String getDescrizione();
    void setDescrizione(String descrizione);
    LocalDate getData();
    void setData(LocalDate data);
    String getLuogo();
    void setLuogo(String luogo);
    Set<Users> getPrenotazioni();
    void setPrenotazioni(Set<Users> prenotazioni);
}
