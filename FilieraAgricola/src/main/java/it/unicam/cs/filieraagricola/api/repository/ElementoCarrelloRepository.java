package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.carrello.ElementoCarrello;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ElementoCarrelloRepository extends JpaRepository<ElementoCarrello, Integer> {
}
