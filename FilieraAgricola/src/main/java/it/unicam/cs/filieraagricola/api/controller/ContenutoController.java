package it.unicam.cs.filieraagricola.api.controller;


import it.unicam.cs.filieraagricola.api.entities.Contenuto;
import it.unicam.cs.filieraagricola.api.entities.Elemento;
import it.unicam.cs.filieraagricola.api.repository.ContenutoRepository;
import it.unicam.cs.filieraagricola.api.repository.ElementoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ContenutoController {
    @Autowired
    private ContenutoRepository contenutoRepository;
    @Autowired
    private ElementoRepository elementoRepository;

    public ContenutoController() {}

    //TODO CAMBIARE IL METODO
    public ResponseEntity<Object> getContenuto() {
        return new ResponseEntity<>(contenutoRepository.findAll(), HttpStatus.OK);
    }


    //TODO CAMBIARE IL METODO
    public ResponseEntity<Object> getContenuto(int id) {
        if (!contenutoRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(contenutoRepository.findById(id), HttpStatus.OK);
        }
    }


    public boolean aggiungiConenuto(Contenuto contenuto) {
        boolean contenutoEsiste = contenutoAlreadyExists(contenuto);
        if (contenutoEsiste) {
            return false;
        }
        contenutoRepository.save(contenuto);
        return true;
    }

    public boolean aggiungiContenutoWithParam(String nome, String descrizione, double prezzo, int idElemento){
        Optional<Contenuto> contenutoOptional = contenutoRepository.findById(idElemento);
        Contenuto contenuto = new Contenuto();
        if (contenutoOptional.isPresent()) { contenuto = contenutoOptional.get(); }
        boolean contenutoEsiste = contenutoAlreadyExists(contenuto);
        if (contenutoEsiste) {
            return false;
        }
        creaContenuto(nome, descrizione, prezzo, idElemento);
        return true;
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

    private void creaContenuto(String nome, String descrizione, double prezzo, int idElemento) {
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
    }

    private boolean contenutoAlreadyExists(Contenuto contenuto) {
        List<Contenuto> contenutos = contenutoRepository.findByNomeAndDescrizioneAndPrezzo(
                contenuto.getNome(), contenuto.getDescrizione(), contenuto.getPrezzo()
        );
        if (!contenutos.isEmpty()) {
            for (Contenuto cont : contenutos) {
                if (cont.getElemento() == contenuto.getElemento()) {
                    return true;
                }
            }
        }
        return false;
    }
}

