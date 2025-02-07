package it.unicam.cs.filieraagricola.api.services.gestore;

import it.unicam.cs.filieraagricola.api.entities.Categoria;
import it.unicam.cs.filieraagricola.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getAllCategorie() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> getCategoriaByNome(String nomeCategoria) {
        return categoriaRepository.findByNome(nomeCategoria);
    }

    public boolean existsCategoria(Integer idCategoria) {
        return categoriaRepository.existsById(idCategoria);
    }

    public boolean existsSameCategoria(String nomeCategoria) {
        return categoriaRepository.existsByNome(nomeCategoria);
    }

    public void saveCategoria(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    public void aggiungiCategoria(String nome) {
        if (categoriaRepository.findByNome(nome).isPresent()) {
            throw new IllegalArgumentException("La categoria esiste già.");
        }
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoriaRepository.save(categoria);
    }

    public void eliminaCategoria(String nome) {
        categoriaRepository.deleteByNome(nome);
    }
}