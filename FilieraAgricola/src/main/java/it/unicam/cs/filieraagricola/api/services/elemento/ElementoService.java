package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.repository.ElementoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElementoService {

    @Autowired
    private ElementoRepository elementoRepository;

    public List<Elemento> getElementi() {
        return elementoRepository.findAll();
    }

    public Optional<Elemento> getElemento(int id) {
        return elementoRepository.findById(id);
    }

    public boolean existsElemento(int id) {
        return elementoRepository.existsById(id);
    }
}
