package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.ElementoCarrello;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElementoCarrelloRepository extends JpaRepository<ElementoCarrello, Integer> {
}
