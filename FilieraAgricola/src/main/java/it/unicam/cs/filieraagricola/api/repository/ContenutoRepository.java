package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Contenuto;
import it.unicam.cs.filieraagricola.api.entities.Elemento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContenutoRepository extends JpaRepository<Contenuto, Integer> {
    @Query("SELECT c FROM Contenuto c WHERE c.elemento = :elemento")
    Optional<Contenuto> findByElemento(
            @Param("elemento") Elemento elemento
            );
}
