package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.Categoria;
import it.unicam.cs.filieraagricola.api.services.gestore.CategoriaService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorie")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> getCategorie() {
        return new ResponseEntity<>(categoriaService.getAllCategorie(), HttpStatus.OK);
    }

    @PostMapping("/aggiungi")
    public ResponseEntity<String> aggiungiCategoria(@RequestParam String nome) {
        if(!categoriaService.existsSameCategoria(nome)){
            categoriaService.aggiungiCategoria(nome);
            return new ResponseEntity<>("Categoria: " + nome + " aggiunta con successo.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Categoria con nome: " + nome +" già esistente", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/elimina")
    public ResponseEntity<String> eliminaCategoria(@RequestParam String nome) {
        if (categoriaService.getCategoriaByNome(nome).isPresent()) {
            categoriaService.eliminaCategoria(nome);
            return new ResponseEntity<>("Categoria: " + nome + " eliminata con successo.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Categoria con nome: " + nome + " non esistente", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/aggiorna")
    public ResponseEntity<String> modificaCategoria(@RequestBody Categoria categoriaAggiornata) {
        if(categoriaService.existsCategoria(categoriaAggiornata.getId())){
            if(!categoriaService.existsSameCategoria(categoriaAggiornata.getNome())){
                categoriaService.saveCategoria(categoriaAggiornata);
                return new ResponseEntity<>("Categoria: " + categoriaAggiornata.getNome() + " aggiornata con successo", HttpStatus.OK);
            } return new ResponseEntity<>("Categoria con nome: " + categoriaAggiornata.getNome() +" già esistente", HttpStatus.CONFLICT);
        } return new ResponseEntity<>("Categoria non trovata", HttpStatus.BAD_REQUEST);
    }
}