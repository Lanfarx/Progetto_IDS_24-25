package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Evento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.Visita;
import it.unicam.cs.filieraagricola.api.repository.UserRepository;
import it.unicam.cs.filieraagricola.api.repository.VisitaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/animatore")
public class VisitaServiceController {

    private final VisitaRepository visitaRepository;
    private final UserRepository userRepository;

    public VisitaServiceController(VisitaRepository visitaRepository, UserRepository userRepository) {
        this.visitaRepository = visitaRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initSampleData() {
        Visita visita = new Visita();
        visita.setTitolo("ProvaVisita");
        visita.setDescrizione("questa è una visita");
        visita.setLuogo("Genzano");
        visita.setData(LocalDate.ofEpochDay(01/01/2003));
        Users user1 = new Users();
        user1.setUsername("prenotato");
        user1.setPassword("prenotato");
        user1.getRoles().add(UserRole.ACQUIRENTE);
        userRepository.save(user1);
        visita.getPrenotazioni().add(user1);
        visitaRepository.save(visita);
    }

    @RequestMapping({"/visita"})
    public ResponseEntity<Object> getVisite() {
        return new ResponseEntity<>(this.visitaRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping({"/visita/{id}"})
    public ResponseEntity<Object> getVisita(@PathVariable("id") int id) {
        if (!this.visitaRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.visitaRepository.findById(id), HttpStatus.OK);
        }
    }

    @PostMapping("/visita/aggiungi")
    public ResponseEntity<Object> aggiungiVisita(@RequestBody Visita visita) {
        if (!this.visitaRepository.existsByTitoloAndDataAndDescrizioneAndLuogo(
                visita.getTitolo(), visita.getData(), visita.getDescrizione(), visita.getLuogo())) {
            this.visitaRepository.save(visita);
            return new ResponseEntity<>("Visita creata", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("La visita già esiste", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/visita/aggiungiconparametri")
    public ResponseEntity<Object> aggiungiVisitaConParametri(
            @RequestParam String titolo,
            @RequestParam String descrizione,
            @RequestParam String luogo,
            @RequestParam LocalDate data) {
        if (!this.visitaRepository.existsByTitoloAndDataAndDescrizioneAndLuogo(
                titolo, data, luogo, descrizione)) {
            Visita visita = new Visita();
            visita.setTitolo(titolo);
            visita.setDescrizione(descrizione);
            visita.setLuogo(luogo);
            visita.setData(data);
            this.visitaRepository.save(visita);
            return new ResponseEntity<>("Visita creata", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("La visita già esiste", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/visita/elimina/{id}")
    public ResponseEntity<Object> eliminaVisita(@PathVariable int id) {
        visitaRepository.deleteById(id);
        return new ResponseEntity<>("Visita eliminato con successo", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/visita/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> aggiornaVisita(@RequestBody Evento visitaAggiornata) {
        if (this.visitaRepository.existsById(visitaAggiornata.getId())) {
            this.visitaRepository.save(visitaAggiornata);
            return new ResponseEntity<>("Evento " + visitaAggiornata.getId() + " Aggiornato", HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("Evento " + visitaAggiornata.getId() + " Non Trovato");
        }
    }
}
