package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.Prodotto;
import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.repository.PacchettoRepository;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.api.services.PacchettoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/distributore")
public class PacchettoController {
    @Autowired
    private PacchettoService pacchettoService;

    public PacchettoController(PacchettoService pacchettoService) {
        this.pacchettoService = pacchettoService;
    }

    /*@PostConstruct
    public void initSampleData() {
        ProdottoBase pomodoro = new ProdottoBase();
        pomodoro.setNome("Pomodoro");
        prodottoBaseRepository.save(pomodoro);
        ProdottoTrasformato polpa = new ProdottoTrasformato();
        polpa.setNome("Polpa");
        polpa.setProdottoBase(pomodoro);
        prodottoTrasformatoRepository.save(polpa);
        Pacchetto samplePacchetto = new Pacchetto();
        samplePacchetto.setNome("Pacchetto");
        samplePacchetto.setDescrizione("Sono un pacchetto di esempio, controlla se tutti i valori sono corretti!o");
        samplePacchetto.addProdotto(pomodoro);
        samplePacchetto.addProdotto(polpa);
        samplePacchetto.setPrezzo(1.0);
        pacchettoRepository.save(samplePacchetto);
        contenutoController.aggiungiContenutoWithParam("paila", "sos", 2, 1);
    }*/

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Benvenuto nella dashboard del Distributore");
    }


    @RequestMapping({"/pacchetti"})
    public ResponseEntity<Object> getPacchetto() {
        return pacchettoService.getPacchetti();
    }

    @RequestMapping({"/pacchetti/{id}"})
    public ResponseEntity<Object> GetPacchetto(@PathVariable("id") int id) {
        return pacchettoService.GetPacchetto(id);
    }

    @PostMapping({"/pacchetti/aggiungi"})
    public ResponseEntity<Object> aggiungiPacchetto(@RequestBody Pacchetto pacchetto) {
        return aggiungiPacchetto(pacchetto);
    }

    @PostMapping({"/pacchetti/aggiungi/prodotto"})
    public ResponseEntity<Object> aggiungiProdotto(@RequestParam ("idPacchetto") int idPacchetto,
                                                    @RequestParam ("idProdotto") int idProdotto){
        return pacchettoService.aggiungiProdotto(idPacchetto, idProdotto);
    }

    @PostMapping({"/pacchetti/aggiungiconparametri"})
    public ResponseEntity<Object> aggiungiPacchettoWithParam(@RequestParam("nome") String nome,
                                                             @RequestParam("descrizione") String descrizione,
                                                             @RequestParam("ProdottiSet") Set<Integer> idProdottiSet){
        return aggiungiPacchettoWithParam(nome, descrizione, idProdottiSet);
    }


    @RequestMapping({"/pacchetti/elimina/{id}"})
    public ResponseEntity<Object> eliminaPacchetto(@RequestParam("id") int id) {
        return pacchettoService.eliminaPacchetto(id);
    }

    @RequestMapping({"/pacchetti/elimina/prodotto/{id}"})
    public ResponseEntity<Object> eliminaProdotto(@RequestParam("id") int id,
                                                  @RequestParam("idProdotto") int idProdotto) {
        return pacchettoService.eliminaProdotto(id, idProdotto);
    }

    @RequestMapping(
            value = {"/pacchetti/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> aggiornaPacchetto(@RequestBody Pacchetto pacchetto) {
        return pacchettoService.aggiornaPacchetto(pacchetto);
    }
}