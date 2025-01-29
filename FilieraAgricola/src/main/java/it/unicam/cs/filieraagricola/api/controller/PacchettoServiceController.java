package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.Prodotto;
import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.repository.PacchettoRepository;
import it.unicam.cs.filieraagricola.api.repository.ProdottoBaseRepository;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.api.repository.ProdottoTrasformatoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/distributore")
public class PacchettoServiceController {
    @Autowired
    private PacchettoRepository pacchettoRepository;
    @Autowired
    private ProdottoBaseRepository prodottoBaseRepository;
    @Autowired
    private ProdottoTrasformatoRepository prodottoTrasformatoRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;

    public PacchettoServiceController(PacchettoRepository pacchettoRepository,
                                      ProdottoBaseRepository prodottoBaseRepository,
                                      ProdottoTrasformatoRepository prodottoTrasformatoRepository,
                                      ProdottoRepository prodottoRepository) {
        this.pacchettoRepository = pacchettoRepository;
        this.prodottoBaseRepository = prodottoBaseRepository; //TODO togliere quando si rimuove InitSampleData
        this.prodottoTrasformatoRepository = prodottoTrasformatoRepository;
        this.prodottoRepository = prodottoRepository;
    }

    @PostConstruct
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
    }

    @RequestMapping({"/pacchetti"})
    public ResponseEntity<Object> getPacchetto() {
        return new ResponseEntity<>(this.pacchettoRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping({"/pacchetti/{id}"})
    public ResponseEntity<Object> GetPacchetto(@PathVariable("id") int id) {
        if (!this.pacchettoRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.pacchettoRepository.findById(id), HttpStatus.OK);
        }
    }

    @PostMapping({"/pacchetti/aggiungi"})
    public ResponseEntity<Object> aggiungiPacchetto(@RequestBody Pacchetto pacchetto) {
        ResponseEntity<Object> BAD_REQUEST = getObjectResponseEntity(pacchetto.getNome(),
                pacchetto.getDescrizione(), pacchetto.getPrezzo(), pacchetto.getProdottiSet());
        return new ResponseEntity<>("Product Created", HttpStatus.CREATED);
    }

    @PostMapping({"/pacchetti/aggiungi/prodotto"})
    public ResponseEntity<Object> aggiungiPacchetto(@RequestParam ("idPacchetto") int idPacchetto,
                                                    @RequestParam ("idProdotto") int idProdotto){

        Optional<Pacchetto> pacchettoOptional = pacchettoRepository.findById(idPacchetto);
        Pacchetto pacchetto = new Pacchetto();
        if (pacchettoOptional.isPresent()) {
            pacchetto = pacchettoOptional.get();
        }
        ResponseEntity<Object> response;
        response = aggiungiProdotto(pacchetto, idProdotto);
        if(response != null) { //In caso di errore, lo mando subito
            return response;
        }
        pacchettoRepository.save(pacchetto);
        return new ResponseEntity<>("Prodotto aggiunto", HttpStatus.CREATED);
    }

    private ResponseEntity<Object> aggiungiProdotto(Pacchetto pacchetto, int idProdotto) {
        Optional<Prodotto> prodottoOptional = prodottoRepository.findById(idProdotto);
        if(prodottoOptional.isEmpty()) {
            return new ResponseEntity<>("Product does not exist", HttpStatus.BAD_REQUEST);
        }
        Prodotto prodotto = prodottoOptional.get();
        if(pacchetto.getProdottiSet().contains(prodotto)) {
            return new ResponseEntity<>("Product already present in the bundle", HttpStatus.BAD_REQUEST);
        }
        pacchetto.addProdotto(prodotto);
        return null; //Null, così da non mostrare alcun problema (response)
    }

    @PostMapping({"/pacchetti/aggiungiconparametri"})
    public ResponseEntity<Object> aggiungiPacchettoWithParam(@RequestParam("nome") String nome,
                                                             @RequestParam("descrizione") String descrizione,
                                                             @RequestParam("prezzo") double prezzo,
                                                             @RequestParam("ProdottiSet") Set<Integer> idProdottiSet){
        //TODO AGGIUNGERE CHECK MINIMO PRODOTTI 2+
        Set<Prodotto> prodottoSet = new HashSet<>();
        for (int i : idProdottiSet) {
            System.out.println(i);
            if(prodottoRepository.existsById(i)){
                if(prodottoRepository.findById(i).isPresent()){
                    prodottoSet.add(prodottoRepository.findById(i).get());
                }
            }
        }
        ResponseEntity<Object> BAD_REQUEST = getObjectResponseEntity(nome, descrizione, prezzo, prodottoSet);
        if (BAD_REQUEST != null) return BAD_REQUEST;
        Pacchetto pacchetto = new Pacchetto();
        pacchetto.setNome(nome);
        pacchetto.setDescrizione(descrizione);
        pacchetto.setProdottiSet(prodottoSet);
        pacchetto.setPrezzo(prezzo);
        this.pacchettoRepository.save(pacchetto);
        return new ResponseEntity<>("Pacchetto creato", HttpStatus.CREATED);
    }

    private ResponseEntity<Object> getObjectResponseEntity(String nome, String descrizione, double prezzo, Set<Prodotto> ProdottiSet) {
        List<Pacchetto> pacchettoList = this.pacchettoRepository.findByNomeAndDescrizioneAndPrezzo(
                nome, descrizione, prezzo);
        for (Pacchetto pacchetto : pacchettoList) {
            if (ProdottiSet.equals(pacchetto.getProdottiSet())) {
                return new ResponseEntity<>("Il pacchetto con gli stessi prodotti esiste già", HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

    @RequestMapping({"/pacchetti/elimina/{id}"})
    public ResponseEntity<Object> eliminaPacchetto(@RequestParam("id") int id) {
        this.pacchettoRepository.deleteById(id);
        return new ResponseEntity<>("Product " + id + " Deleted", HttpStatus.OK);
    }

    @RequestMapping({"/pacchetti/elimina/prodotto/{id}"})
    public ResponseEntity<Object> eliminaProdotto(@RequestParam("id") int id,
                                                  @RequestParam("idProdotto") int idProdotto) {
        this.pacchettoRepository.deleteById(id);
        return new ResponseEntity<>("Product " + id + " Deleted", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/pacchetti/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> aggiornaPacchetto(@RequestBody Pacchetto pacchetto) {
        if (this.pacchettoRepository.existsById(pacchetto.getId())) {
            this.pacchettoRepository.save(pacchetto);
            return new ResponseEntity<>("Product " + pacchetto.getId() + " Updated", HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("Prodotto " + pacchetto.getId() + " Not Found");
        }
    }
}