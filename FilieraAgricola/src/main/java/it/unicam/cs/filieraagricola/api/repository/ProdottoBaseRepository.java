
package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoBaseRepository extends JpaRepository<ProdottoBase, Integer> {
        boolean existsByNomeAndCertificazioniAndMetodiDiColtivazioneAndPrezzo(String nome, String certificazioni, String metodiColtivazione, double prezzo);
    }
