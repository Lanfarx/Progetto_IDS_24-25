package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CarrelloRepository extends JpaRepository<Carrello, Integer> {
    Carrello findByUserId(int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ElementoCarrello ec WHERE ec.elemento.id = :idElemento")
    void rimuoviElementoDaCarrelli(int idElemento);
}
