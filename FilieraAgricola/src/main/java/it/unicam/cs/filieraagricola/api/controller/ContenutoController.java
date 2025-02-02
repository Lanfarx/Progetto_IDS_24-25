package it.unicam.cs.filieraagricola.api.controller;


import it.unicam.cs.filieraagricola.api.entities.Contenuto;
import it.unicam.cs.filieraagricola.api.services.ContenutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//TODO valutare se necessario
@RestController
public class ContenutoController {
    @Autowired
    private ContenutoService contenutoService;

    public ContenutoController() {}


    public List<Contenuto> getContenuto() {
        return contenutoService.getContenuto();
    }

    public Contenuto getContenuto(int id) {
        return contenutoService.getContenuto(id);
    }

    public boolean aggiungiConenuto(Contenuto contenuto) {
        return contenutoService.aggiungiConenuto(contenuto);
    }

    public boolean aggiungiContenutoWithParam(String nome, String descrizione, double prezzo, int idElemento){
        return contenutoService.aggiungiContenutoWithParam(nome, descrizione, prezzo, idElemento);
    }

    public boolean eliminaContenuto(int id) {
       return contenutoService.eliminaContenuto(id);
    }

    public boolean aggiornaContenuto(Contenuto contenuto) {
        return contenutoService.aggiornaContenuto(contenuto);
    }
}

