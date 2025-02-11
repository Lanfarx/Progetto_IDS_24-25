package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ElementoRepository extends JpaRepository<Elemento, Integer> {
    List<Elemento> findElementiByStatorichiestaEquals(StatoContenuto statorichiesta);

    boolean existsByIdAndStatorichiesta(int id, StatoContenuto statorichiesta);
}
