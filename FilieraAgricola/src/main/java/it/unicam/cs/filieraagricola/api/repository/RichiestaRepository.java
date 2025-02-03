package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RichiestaRepository extends JpaRepository<Richiesta, Integer> {
    List<Richiesta> findByStato(StatoRichiesta stato);

    @Query("SELECT r FROM RichiestaEliminazione r")
    List<Richiesta> findAllRichiesteEliminazione();

    @Query("SELECT r FROM RichiestaRuolo r")
    List<Richiesta> findAllRichiesteRuolo();
}