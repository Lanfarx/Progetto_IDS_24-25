package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrelloRepository extends JpaRepository<Carrello, Integer> {
    Carrello findByUserId(int userId);
}
