
package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.ProdottoTrasformato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProdottoTrasformatoRepository extends JpaRepository<ProdottoTrasformato, Integer> {
    boolean existsByNomeAndProcessoTrasformazioneAndCertificazioniAndPrezzoAndProdottoBase(String nome, String processoTrasformazione, String certificazioni, double prezzo, ProdottoBase prodottoBase);
}
