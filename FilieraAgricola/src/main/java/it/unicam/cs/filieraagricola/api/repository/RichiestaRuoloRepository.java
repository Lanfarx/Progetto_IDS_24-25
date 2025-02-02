package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.commons.StatoRichiesta;
import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RichiestaRuoloRepository extends JpaRepository<RichiestaRuolo, Integer> {
    List<RichiestaRuolo> findByStato(StatoRichiesta stato);

    boolean existsByUserAndRuoloRichiesto(Users user, UserRole ruolo);
}
