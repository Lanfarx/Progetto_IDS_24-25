package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Contenuto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenutoRepository extends JpaRepository<Contenuto, Integer> {
    List<Contenuto> findByNomeAndDescrizioneAndPrezzo (String nome, String descrizione, double prezzo);
}
