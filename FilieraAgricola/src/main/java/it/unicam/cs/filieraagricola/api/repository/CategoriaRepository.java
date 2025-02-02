package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Optional<Categoria> findByNome(String nome);

    boolean existsByNome(String nome);
}