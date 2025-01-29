package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.Visita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Set;

public interface VisitaRepository extends JpaRepository<Visita, Integer> {
    boolean existsByTitoloAndDataAndDescrizioneAndLuogo(String titolo, LocalDate data, String descrizione, String luogo);
}
