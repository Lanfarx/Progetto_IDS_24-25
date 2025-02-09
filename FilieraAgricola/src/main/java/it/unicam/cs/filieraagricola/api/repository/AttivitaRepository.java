package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttivitaRepository extends JpaRepository<Visita, Integer> {

    @Query("SELECT v FROM Visita v WHERE TYPE(v) = Evento")
    List<Visita> findAllEventi();

    @Query("SELECT v FROM Visita v WHERE TYPE(v) = Visita")
    List<Visita> findAllVisite();

    List<Visita> findByOrganizzatore(Users organizzatore);

    List<Visita> findAllByPrenotazioniContains (Users user);

    @Query("SELECT COUNT(v) = 1 FROM Visita v WHERE v.id = :id")
    boolean existsVisitaById(@Param("id") int id);

    @Query("SELECT v FROM Evento v WHERE v.id = :id")
    Optional<Visita> findEventoById(@Param("id") int id);

    @Query("SELECT COUNT(v) = 1 FROM Visita v WHERE TYPE(v) = Evento AND v.id = :id")
    boolean existsEventoById(@Param("id") int id);

    boolean existsByTitoloAndDataAndLuogo(String titolo, LocalDate data, String luogo);
}
