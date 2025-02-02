package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Elemento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementoRepository extends JpaRepository<Elemento, Integer> {
}
