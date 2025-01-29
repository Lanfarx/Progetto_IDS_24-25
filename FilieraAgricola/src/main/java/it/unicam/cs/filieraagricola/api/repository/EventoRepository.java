package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Evento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Set;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
    boolean existsByTitoloAndDataAndDescrizioneAndLuogo(String titolo, LocalDate data, String descrizione, String luogo);
}