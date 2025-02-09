package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.carrello.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdineRepository extends JpaRepository<Ordine, Integer> {
    List<Ordine> findByUserId(int userId);
}
