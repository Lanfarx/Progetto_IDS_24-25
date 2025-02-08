package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RichiestaRepository extends JpaRepository<Richiesta, Integer> {

    @Query("SELECT r from RichiestaRuolo r WHERE r.stato = :stato")
    List<RichiestaRuolo> findRichiestaRuoloByStato(StatoContenuto stato);

    @Query("SELECT r from RichiestaRuolo r WHERE r.id = :id")
    Optional<RichiestaRuolo> findRichiestaRuoloById(Integer id);

    @Query("SELECT CASE WHEN COUNT(r) = 1 THEN TRUE ELSE FALSE END " +
            "FROM RichiestaRuolo r WHERE r.id = :id")
    boolean existsRichiestaRuoloById(Integer id);

    @Query("SELECT CASE WHEN COUNT(r) = 1 THEN TRUE ELSE FALSE END " +
            "FROM RichiestaRuolo r WHERE r.user = :user AND r.ruoloRichiesto = :ruolo")
    boolean existsRichiestRuoloByUserAndRuoloRichiesto(Users user, UserRole ruolo);

    @Query("SELECT r from RichiestaEliminazione r WHERE r.id = :id")
    Optional<RichiestaEliminazione> findRichiestaEliminazioneById(Integer id);

    @Query("SELECT r from RichiestaEliminazione r WHERE r.stato = :stato")
    List<RichiestaEliminazione> findRichiestaEliminazioneByStato(StatoContenuto stato);

    @Query("SELECT CASE WHEN COUNT(r) = 1 THEN TRUE ELSE FALSE END " +
            "FROM RichiestaEliminazione r WHERE r.id = :id")
    boolean existsRichiestaEliminazioneById(Integer id);

    @Query("SELECT CASE WHEN COUNT(r) = 1 THEN TRUE ELSE FALSE END " +
            "FROM RichiestaEliminazione r WHERE r.user = :user AND r.motivazione = :motivazione")
    boolean existsRichiestaEliminazioneByUserAndMotivazione(Users user, String motivazione);

    @Query("SELECT CASE WHEN COUNT(r) = 1 THEN TRUE ELSE FALSE END " +
            "FROM RichiestaValidazione r WHERE r.user = :user AND r.elemento = :valore")
    boolean existsRichiestaValidazioneByUserAndElemento(Users user, Elemento valore);

    @Query("SELECT CASE WHEN COUNT(r) = 1 THEN TRUE ELSE FALSE END " +
            "FROM RichiestaValidazione r WHERE r.id = :id")
    boolean existsRichiestaValidazioneById(Integer id);

    @Query("SELECT r from RichiestaValidazione r WHERE r.stato = :stato")
    List<RichiestaValidazione> findRichiestaValidazioneByStato(StatoContenuto stato);

    @Query("SELECT r from RichiestaValidazione r WHERE r.id = :id")
    Optional<RichiestaValidazione> findRichiestaValidazioneById(Integer id);

    List<Richiesta> findByUser(Users user);

    @Query("SELECT r from RichiestaRuolo r WHERE r.user = :currentUser")
    List<RichiestaRuolo> findRichiesteRuoloByUser(Users currentUser);

    @Query("SELECT r from RichiestaEliminazione r WHERE r.user = :currentUser")
    List<RichiestaEliminazione> findRichiesteEliminazioneByUser(Users currentUser);

    @Query("SELECT r from RichiestaValidazione r WHERE r.user = :currentUser")
    List<RichiestaValidazione> findRichiesteValidazioneByUser(Users currentUser);
}