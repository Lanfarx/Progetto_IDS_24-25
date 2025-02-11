package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.repository.ElementoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElementoService<T extends Elemento> {

    @Autowired
    protected ElementoRepository elementoRepository;

    public List<T> getElementiValidi() {
        return (List<T>) elementoRepository.findElementiByStatorichiestaEquals(StatoContenuto.ACCETTATA);
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
        if(existsElementoAndValido(elemento.getId())) {
            return elemento.getQuantita()>=quantita;
        } else throw new IllegalArgumentException("Elemento non esistente");
    }

    public boolean existsElementoAndValido(int id) {
        return elementoRepository.existsByIdAndStatorichiesta(id, StatoContenuto.ACCETTATA);
    }

    public boolean existsElementoAndAttesa(Integer id) {
        return elementoRepository.existsByIdAndStatorichiesta(id, StatoContenuto.ATTESA);
    }
}
