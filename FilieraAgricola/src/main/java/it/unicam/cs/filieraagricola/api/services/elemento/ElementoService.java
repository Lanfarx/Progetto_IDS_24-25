package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.repository.ElementoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElementoService<T extends Elemento> {

    @Autowired
    protected ElementoRepository elementoRepository;

    public List<T> getElementi() {
        return (List<T>) elementoRepository.findAll();
    }

    public Optional<T> getElemento(int id) {
        return (Optional<T>) elementoRepository.findById(id);
    }

    public boolean existsElemento(int id) {
        return elementoRepository.existsById(id);
    }

    public void eliminaElemento(int id) {
        elementoRepository.deleteById(id);
    }

    public boolean checkDisponibilita(Elemento elemento, int quantita) {
        if(existsElemento(elemento.getId())) {
            return elemento.getQuantita()>=quantita;
        } else throw new IllegalArgumentException("Elemento non esistente");
    }
}
