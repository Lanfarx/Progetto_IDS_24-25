package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RichiestaRepository extends JpaRepository<Richiesta, Integer> {
    List<Richiesta> findByStato(StatoRichiesta stato);

    @Query("SELECT r from RichiestaRuolo r WHERE r.stato = :stato")
    List<RichiestaRuolo> findRichiestaRuoloByStato(StatoRichiesta stato);

    @Query("SELECT r from RichiestaRuolo r WHERE r.id = :id")
    Optional<RichiestaRuolo> findRichiestaRuoloById(Integer id);

    @Query("SELECT r from RichiestaEliminazione r WHERE r.id = :id")
    Optional<RichiestaEliminazione> findRichiestaEliminazioneById(Integer id);

    @Query("SELECT r from RichiestaEliminazione r WHERE r.stato = :stato")
    List<RichiestaEliminazione> findRichiestaEliminazioneByStato(StatoRichiesta stato);

    @Query("SELECT CASE WHEN COUNT(r) = 1 THEN TRUE ELSE FALSE END " +
            "FROM RichiestaRuolo r WHERE r.id = :id")
    boolean existsRichiestaRuoloById(Integer id);

    @Query("SELECT CASE WHEN COUNT(r) = 1 THEN TRUE ELSE FALSE END " +
            "FROM RichiestaRuolo r WHERE r.user = :user AND r.ruoloRichiesto = :ruolo")
    boolean existsRichiestRuoloByUserAndRuoloRichiesto(Users user, UserRole ruolo);

    @Query("SELECT CASE WHEN COUNT(r) = 1 THEN TRUE ELSE FALSE END " +
            "FROM RichiestaEliminazione r WHERE r.user = :user")
    boolean existsRichiestaEliminazioneByUserAndMotivazione(Users user);

    @Query("SELECT CASE WHEN COUNT(r) = 1 THEN TRUE ELSE FALSE END " +
            "FROM RichiestaEliminazione r WHERE r.id = :id")
    boolean existsRichiestaEliminazioneById(Integer id);

    @Query("SELECT r FROM RichiestaRuolo r")
    List<Richiesta> findAllRichiesteRuolo();

    @Query("SELECT r FROM RichiestaEliminazione r")
    List<Richiesta> findAllRichiesteEliminazione();
}