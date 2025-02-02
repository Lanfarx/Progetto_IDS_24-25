package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.entities.Contenuto;
import it.unicam.cs.filieraagricola.api.entities.Elemento;
import it.unicam.cs.filieraagricola.api.repository.ContenutoRepository;
import it.unicam.cs.filieraagricola.api.repository.ElementoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class ContenutoService {
    @Autowired
    private ContenutoRepository contenutoRepository;
    @Autowired
    private ElementoRepository elementoRepository;

    public List<Contenuto> getContenuto() { return contenutoRepository.findAll(); }

    public Contenuto getContenuto(int id) {
        if (!contenutoRepository.existsById(id)) {
            return null;
        } else {
            Optional<Contenuto> contenuto = contenutoRepository.findById(id);
            return contenuto.get(); //Il controllo di esistenza viene effettuato sopra
        }
    }

    public Contenuto getContenutoByParam(Elemento elemento){
        Optional<Contenuto> contenutoOptional = contenutoRepository.findByParams(elemento.getNome(), elemento.getDescrizione(), elemento.getPrezzo(), elemento);
        return contenutoOptional.orElse(null);
    }

    public boolean aggiungiConenuto(Contenuto contenuto) {
        boolean contenutoEsiste = contenutoAlreadyExists(contenuto.getNome(), contenuto.getDescrizione(),
                contenuto.getPrezzo(), contenuto.getId());
        if (contenutoEsiste) {
            return false;
        }
        contenutoRepository.save(contenuto);
        return true;
    }

    public boolean aggiungiContenutoWithParam(String nome, String descrizione, double prezzo, int idElemento){
        return creaContenuto(nome, descrizione, prezzo, idElemento);
    }

    public boolean aggiungiContenutoDaElemento(Elemento elemento){
        return creaContenuto(elemento.getNome(), elemento.getDescrizione(), elemento.getPrezzo(), elemento.getId());
    }

    public boolean eliminaContenuto(int id) {
        contenutoRepository.deleteById(id);
        return true;
    }

    public boolean aggiornaContenuto(Contenuto contenuto) {
        if (contenutoRepository.existsById(contenuto.getId())) {
            contenutoRepository.save(contenuto);
            return true;
        } else {
            return false;
        }
    }

    public boolean verificaContenuto(int idContenuto){
        if(!contenutoRepository.existsById(idContenuto)){
            return false;
        }
        Contenuto contenuto = contenutoRepository.getReferenceById(idContenuto);
        if(contenuto.isVerificato()){
            return false;
        }
        contenuto.setVerificato(true);
        return true;
    }


    private boolean creaContenuto(String nome, String descrizione, double prezzo, int idElemento) {
        if (contenutoAlreadyExists(nome, descrizione, prezzo, idElemento)) {
            return false;
        }
        Optional<Elemento> elementoOpt = elementoRepository.findById(idElemento);
        Elemento elemento = null;
        if (elementoOpt.isPresent()) {
            elemento = elementoOpt.get();
        }
        Contenuto contenuto = new Contenuto();
        contenuto.setNome(nome);
        contenuto.setDescrizione(descrizione);
        contenuto.setPrezzo(prezzo);
        contenuto.setElemento(elemento);
        contenuto.setVerificato(false);
        contenutoRepository.save(contenuto);
        return true;
    }

    private boolean contenutoAlreadyExists(String nome, String descrizione, double prezzo, int idElemento) {
        List<Contenuto> contenutos = contenutoRepository.findByNomeAndDescrizioneAndPrezzo(
                nome, descrizione, prezzo);
        Optional<Elemento> elementoOptional = elementoRepository.findById(idElemento);
        if(elementoOptional.isEmpty()){
            throw new InvalidParameterException("Elemento con id: " + idElemento + " non trovato");
        }
        Elemento elemento = elementoOptional.get();
        if (!contenutos.isEmpty()) {
            for (Contenuto cont : contenutos) {
                if (cont.getElemento() == elemento) {
                    return true;
                }
            }
        }
        return false;
    }

}
