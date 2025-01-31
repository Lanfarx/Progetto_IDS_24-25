package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.Evento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.Visita;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.AttivitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/animatore")
public class AttivitaController {

    private final AttivitaService attivitaService;
    private final UserService userService;

    public AttivitaController(AttivitaService attivitaService, UserService userService) {
        this.attivitaService = attivitaService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllAttivita() {
        return new ResponseEntity<>(attivitaService.getAllAttivita(), HttpStatus.OK);
    }

    @GetMapping("/visite")
    public ResponseEntity<Object> getVisite() {
        return new ResponseEntity<>(attivitaService.getAllVisite(), HttpStatus.OK);
    }

    @GetMapping("/eventi")
    public ResponseEntity<Object> getEventi() {
        return new ResponseEntity<>(attivitaService.getAllEventi(), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Object> getAttivita(@PathVariable("id") int id) {
        if (attivitaService.existsVisita(id)) {
            return new ResponseEntity<>(attivitaService.getVisitaOEventoById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/visite/aggiungi")
    public ResponseEntity<Object> aggiungiVisita(@RequestBody Visita visita) {
        if (attivitaService.existsVisitaByParams(visita.getTitolo(), visita.getData(), visita.getDescrizione(), visita.getLuogo())) {
            return new ResponseEntity<>("La visita già esiste", HttpStatus.BAD_REQUEST);
        }

        attivitaService.saveVisita(visita);
        return new ResponseEntity<>("Visita creata", HttpStatus.CREATED);
    }

    @PostMapping("/eventi/aggiungi")
    public ResponseEntity<Object> aggiungiEvento(@RequestBody Evento evento) {
        if (attivitaService.existsVisitaByParams(evento.getTitolo(), evento.getData(), evento.getDescrizione(), evento.getLuogo())) {
            return new ResponseEntity<>("L'evento già esiste", HttpStatus.BAD_REQUEST);
        }
        Set<Users> operatori = saveAndGetOperatori(evento);
        return new ResponseEntity<>("Evento creato con " + operatori.size() + " invitati", HttpStatus.CREATED);
    }

    private Set<Users> saveAndGetOperatori(Evento evento) {
        Set<Users> invitati = evento.getInvitati();
        Set<Integer> idInvitati = new HashSet<>();
        for (Users invitato : invitati) {
            idInvitati.add(invitato.getId());
        }
        Set<Users> operatori =  userService.getOperatoriByIds(idInvitati);
        evento.setInvitati(operatori);
        attivitaService.saveEvento(evento);
        return operatori;
    }

    @PostMapping("/aggiungiconparametri")
    public ResponseEntity<Object> aggiungiVisitaOEventoConParametri(
            @RequestParam String titolo,
            @RequestParam String descrizione,
            @RequestParam String luogo,
            @RequestParam LocalDate data,
            @RequestParam(required = false) Set<Integer> idInvitati) {

        if (attivitaService.existsVisitaByParams(titolo, data, descrizione, luogo)) {
            return new ResponseEntity<>("L'Attivita già esiste", HttpStatus.BAD_REQUEST);
        }
        Visita visita;
        if (idInvitati != null && !idInvitati.isEmpty()) {
            visita = new Evento();
            Set<Users> operatori = new HashSet<>();
            Set<Integer> nonOperatori = new HashSet<>();

            userService.processaInvitati(idInvitati, operatori, nonOperatori);

            ((Evento) visita).setInvitati(operatori);

            String responseMessage = "Evento creato";
            if (!nonOperatori.isEmpty()) {
                responseMessage += ". Tuttavia, gli utenti con ID " + nonOperatori + " non sono stati aggiunti perché non hanno i ruoli richiesti.";
            }
            visita.setTitolo(titolo);
            visita.setDescrizione(descrizione);
            visita.setLuogo(luogo);
            visita.setData(data);
            attivitaService.saveVisita(visita);
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
        } else {
            visita = new Visita();
            visita.setTitolo(titolo);
            visita.setDescrizione(descrizione);
            visita.setLuogo(luogo);
            visita.setData(data);
            attivitaService.saveVisita(visita);
            return new ResponseEntity<>("Visita creata", HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/elimina/{id}")
    public ResponseEntity<Object> eliminaVisitaOEvento(@PathVariable int id) {
        attivitaService.deleteVisita(id);
        return new ResponseEntity<>("Attivita eliminata con successo", HttpStatus.OK);
    }

    @PutMapping("/visite/aggiorna")
    public ResponseEntity<Object> aggiornaVisita(@RequestBody Visita visitaAggiornata) {
        if (attivitaService.existsVisita(visitaAggiornata.getId())) {
            attivitaService.saveVisita(visitaAggiornata);
            return new ResponseEntity<>("Visita " + visitaAggiornata.getId() + " Aggiornata", HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("Visita " + visitaAggiornata.getId() + " Non Trovata");
        }
    }

    @PutMapping("/eventi/aggiorna")
    public ResponseEntity<Object> aggiornaEvento(@RequestBody Evento eventoAggiornato) {
        if (attivitaService.existsEvento(eventoAggiornato.getId())) {
            Set<Users> operatori = saveAndGetOperatori(eventoAggiornato);
            return new ResponseEntity<>("Evento " + eventoAggiornato.getId() + " Aggiornato con " + operatori.size() + " invitati", HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("Evento " + eventoAggiornato.getId() + " Non Trovato");
        }
    }

    @PostMapping("/evento/{id}/invitato")
    public ResponseEntity<Object> aggiungiInvitato(@PathVariable int id, @RequestParam int userId) {
        if (attivitaService.existsEvento(id)) {
            if (userService.existsUser(userId)) {
                Evento evento = (Evento) attivitaService.getEventoById(id).get();
                Users user = userService.getUserById(userId).get();
                if (userService.isOperatore(user)) {
                    attivitaService.addInvitatoToEvento(evento, user);
                    return new ResponseEntity<>("Operatore " + userId + " Invitato all'Evento " + id, HttpStatus.CREATED);
                } else return new ResponseEntity<>("Utente " + userId + " Non è un Operatore (Produttore, Trasformatore, Distributore)", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Utente " + userId + " Non Trovato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Evento " + id + " Non Trovato", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/evento/{id}/eliminainvitato")
    public ResponseEntity<Object> eliminaInvitato(@PathVariable int id, @RequestParam int userId) {
        if (attivitaService.existsEvento(id)) {
            if (userService.existsUser(userId)) {
                Evento evento = (Evento) attivitaService.getEventoById(id).get();
                Users user = userService.getUserById(userId).get();
                if (userService.isOperatore(user)) {
                    attivitaService.removeInvitatoFromEvento(evento, user);
                    return new ResponseEntity<>("Operatore " + userId + " Eliminato dall'Evento " + id, HttpStatus.CREATED);
                } else return new ResponseEntity<>("Utente " + userId + " Non è un Operatore (Produttore, Trasformatore, Distributore)", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Utente " + userId + " Non Trovato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Evento " + id + " Non Trovato", HttpStatus.BAD_REQUEST);
    }
}
