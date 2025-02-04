
package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PacchettoRepository extends JpaRepository<Pacchetto, Integer> {
    List<Pacchetto> findByNomeAndDescrizione(String nome, String descrizione);


    @Query("SELECT p FROM Pacchetto p JOIN p.prodottiSet pr WHERE pr.id = :prodottoId")
    List<Pacchetto> findPacchettiByProdottoId(@Param("prodottoId") Integer prodottoId);
}
