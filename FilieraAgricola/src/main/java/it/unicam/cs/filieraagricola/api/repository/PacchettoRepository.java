
package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PacchettoRepository extends JpaRepository<Pacchetto, Integer> {
    List<Pacchetto> findByNomeAndDescrizione(String nome, String descrizione);

    List<Pacchetto> findByOperatore(Users operatore);

    @Query("SELECT p FROM Pacchetto p JOIN p.prodottiSet pr WHERE pr.id = :prodottoId")
    Set<Pacchetto> findPacchettiByProdottoId(@Param("prodottoId") Integer prodottoId);
}
