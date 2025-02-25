
package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.entities.elemento.Prodotto;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoTrasformato;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdottoRepository extends JpaRepository<Prodotto, Integer> {

    List<ProdottoBase> findProdottiBaseByOperatore(Users operatore);
    List<ProdottoTrasformato> findProdottiTrasformatiByOperatore(Users operatore);
    Prodotto findProdottoById(Integer id);
    void deleteProdottoById(Integer id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM ProdottoBase p WHERE p.id = :id")
    Boolean existsProdottoBaseById(@Param("id") Integer id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM ProdottoTrasformato p WHERE p.id = :id")
    Boolean existsProdottoTrasformatoById(@Param("id") Integer id);

    // Restituisce un ProdottoBase dato il suo ID
    @Query("SELECT p FROM ProdottoBase p WHERE p.id = :id")
    Optional<ProdottoBase> findProdottoBaseById(@Param("id") Integer id);

    // Restituisce un ProdottoTrasformato dato il suo ID
    @Query("SELECT p FROM ProdottoTrasformato p WHERE p.id = :id")
    Optional<ProdottoTrasformato> findProdottoTrasformatoById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProdottoTrasformato p WHERE p.prodottoBase.id = :id")
    void deleteProdottiTrasformatiByProdottoBaseId(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProdottoBase p WHERE p.id = :id")
    void deleteProdottoBaseById(@Param("id") int id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM ProdottoTrasformato p WHERE " +
            "p.nome LIKE %:nome% AND " +
            "p.processoTrasformazione LIKE %:processoTrasformazione% AND " +
            "p.certificazioni LIKE %:certificazioni% AND " +
            "p.prodottoBase = :prodottoBase")
    Boolean existsByCaratteristicheTrasformato(
            @Param("nome") String nome,
            @Param("processoTrasformazione") String processoTrasformazione,
            @Param("certificazioni") String certificazioni,
            @Param("prodottoBase") ProdottoBase prodottoBase);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM ProdottoBase p WHERE " +
            "p.nome LIKE %:nome% AND " +
            "p.metodiDiColtivazione LIKE %:processoTrasformazione% AND " +
            "p.certificazioni LIKE %:certificazioni%")
    Boolean existsByCaratteristicheBase(
            @Param("nome") String nome,
            @Param("processoTrasformazione") String processoTrasformazione,
            @Param("certificazioni") String certificazioni);

    List<Prodotto> findByStatorichiestaEquals(StatoContenuto statorichiesta);

    List<ProdottoBase> findProdottiBaseByStatorichiestaEquals(StatoContenuto statoContenuto);

    List<ProdottoTrasformato> findProdottiTrasformatoByStatorichiestaEquals(StatoContenuto statoContenuto);
}
