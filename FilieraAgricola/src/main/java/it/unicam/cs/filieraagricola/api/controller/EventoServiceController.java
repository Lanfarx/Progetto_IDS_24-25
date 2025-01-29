package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.*;
import it.unicam.cs.filieraagricola.api.repository.EventoRepository;
import it.unicam.cs.filieraagricola.api.repository.UserRepository;
import it.unicam.cs.filieraagricola.api.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/animatore")
public class EventoServiceController {

    private final EventoRepository eventoRepository;
    private final UserRepository userRepository;

    public EventoServiceController(EventoRepository eventoRepository,
                                   UserRepository userRepository, UserService userService) {
        this.eventoRepository = eventoRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initSampleData() {
        Evento evento = new Evento();
        evento.setTitolo("Baudo");
        evento.setDescrizione("questo è un evento");
        evento.setLuogo("Civitanova");
        evento.setData(LocalDate.ofEpochDay(26/06/2003));
        eventoRepository.save(evento);

    }

    private boolean isOperatore(Users user) {
        return user.getRoles().contains(UserRole.PRODUTTORE) ||
                user.getRoles().contains(UserRole.TRASFORMATORE) ||
                user.getRoles().contains(UserRole.DISTRIBUTORE_DI_TIPICITA);
    }

    @RequestMapping({"/evento"})
    public ResponseEntity<Object> getEventi() {
        return new ResponseEntity<>(this.eventoRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping({"/evento/{id}"})
    public ResponseEntity<Object> getEvento(@PathVariable("id") int id) {
        if (!this.eventoRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(this.eventoRepository.findById(id), HttpStatus.OK);
        }
    }

    @PostMapping("/evento/aggiungi")
    public ResponseEntity<Object> aggiungiEvento(@RequestBody Evento evento) {
        if (!this.eventoRepository.existsByTitoloAndDataAndDescrizioneAndLuogo(
                evento.getTitolo(), evento.getData(), evento.getDescrizione(), evento.getLuogo())) {
            this.eventoRepository.save(evento);
            return new ResponseEntity<>("Evento creato", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("L'evento già esiste", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/evento/aggiungiconparametri")
    public ResponseEntity<Object> aggiungiEventoConParametri(
            @RequestParam String titolo,
            @RequestParam String descrizione,
            @RequestParam String luogo,
            @RequestParam LocalDate data,
            @RequestParam Set<Integer> idInvitati) {
        if (!this.eventoRepository.existsByTitoloAndDataAndDescrizioneAndLuogo(
                titolo, data, luogo, descrizione)) {
            Evento evento = new Evento();
            evento.setTitolo(titolo);
            evento.setDescrizione(descrizione);
            evento.setLuogo(luogo);
            evento.setData(data);
            Set<Users> operatori = new HashSet<>();
            Set<Integer> nonOperatori = new HashSet<>();
            for (Integer id : idInvitati) {
                if (this.userRepository.existsById(id)) {
                    Users user = userRepository.findById(id).get();
                    if (isOperatore(user)) {
                        operatori.add(user);
                    } else {
                        nonOperatori.add(id);
                    }
                }
            }
            evento.setInvitati(operatori);
            this.eventoRepository.save(evento);
            String responseMessage = "Evento creato";
            if (!nonOperatori.isEmpty()) {
                responseMessage += ". Tuttavia, gli utenti con ID " + nonOperatori + " non sono stati aggiunti poiché non hanno i ruoli richiesti.";
            }
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("L'evento esiste già", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/evento/elimina/{id}")
    public ResponseEntity<Object> eliminaEvento(@PathVariable int id) {
        eventoRepository.deleteById(id);
        return new ResponseEntity<>("Evento eliminato con successo", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/evento/aggiorna"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> aggiornaEvento(@RequestBody Evento eventoAggiornato) {
        if(this.eventoRepository.existsById(eventoAggiornato.getId())) {
            this.eventoRepository.save(eventoAggiornato);
            return new ResponseEntity<>("Evento " + eventoAggiornato.getId() + " Aggiornato", HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("Evento " + eventoAggiornato.getId() + " Non Trovato");
        }
    }

    @PostMapping("/evento/{id}/invitato")
    public ResponseEntity<Object> aggiungiInvitato(@PathVariable int id, @RequestParam int userId) {
        if (this.eventoRepository.existsById(id)) {
            if (this.userRepository.existsById(userId)) {
                Evento evento = this.eventoRepository.findById(id).get();
                Users user = this.userRepository.findById(userId).get();
                if (isOperatore(user)) {
                    evento.getInvitati().add(user);
                    this.eventoRepository.save(evento);
                    return new ResponseEntity<>("Operatore " + userId + " Invitato all'Evento " + id, HttpStatus.CREATED);
                } else return new ResponseEntity<>("Utente " + userId + " Non è un Operatore (Produttore, Trasformatore, Distributore)", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Utente " + userId + " Non Trovato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Evento " + id + " Non Trovato", HttpStatus.BAD_REQUEST);
    }
}