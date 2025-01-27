
package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PacchettoRepository extends JpaRepository<Pacchetto, Integer> {
    List<Pacchetto> findByNomeAndDescrizioneAndPrezzo(String nome, String descrizione, double prezzo);
}
