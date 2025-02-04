package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.attivita.Evento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.gestore.AttivitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    @GetMapping("/mie-attivita")
    public ResponseEntity<Object> getMieAttivita() {
        Users organizzatore = userService.getCurrentUser();
        if (organizzatore == null) {
            return new ResponseEntity<>("Utente non trovato", HttpStatus.UNAUTHORIZED);
        }

        List<Visita> mieAttivita = attivitaService.getAttivitaByOrganizzatore(organizzatore);
        return new ResponseEntity<>(mieAttivita, HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Object> getAttivita(@PathVariable("id") int id) {
        if (attivitaService.existsAttivita(id)) {
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
        Users organizzatore = userService.getCurrentUser();
        attivitaService.saveVisita(visita, organizzatore);
        return new ResponseEntity<>("Visita creata", HttpStatus.CREATED);
    }

    private Set<Users> saveAndGetOperatori(Evento evento) {
        Set<Users> invitati = evento.getInvitati();
        Set<Integer> idInvitati = new HashSet<>();
        for (Users invitato : invitati) {
            idInvitati.add(invitato.getId());
        }
        Set<Users> operatori = userService.getOperatoriByIds(idInvitati);
        evento.setInvitati(operatori);
        Users organizzatore = userService.getCurrentUser();
        attivitaService.saveEvento(evento, organizzatore);
        return operatori;
    }

    @PostMapping("/eventi/aggiungi")
    public ResponseEntity<Object> aggiungiEvento(@RequestBody Evento evento) {
        if (attivitaService.existsVisitaByParams(evento.getTitolo(), evento.getData(), evento.getDescrizione(), evento.getLuogo())) {
            return new ResponseEntity<>("L'evento già esiste", HttpStatus.BAD_REQUEST);
        }
        Set<Users> operatori = saveAndGetOperatori(evento);
        return new ResponseEntity<>("Evento creato con " + operatori.size() + " invitati", HttpStatus.CREATED);
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
        Users organizzatore = userService.getCurrentUser();
        Visita visita;
        if (idInvitati != null && !idInvitati.isEmpty()) {
            visita = new Evento();
            Set<Users> operatori = new HashSet<>();
            Set<Integer> nonOperatori = new HashSet<>();

            userService.processaInvitati(idInvitati, operatori, nonOperatori);

            ((Evento) visita).setInvitati(operatori);

            String responseMessage = "Evento creato";
            if (!nonOperatori.isEmpty()) {
                responseMessage += ". Tuttavia, gli utenti con ID " + nonOperatori + " non sono stati aggiunti perché non sono operatori (Produttore, Trasformatore, Distributore).";
            }
            visita.setTitolo(titolo);
            visita.setDescrizione(descrizione);
            visita.setLuogo(luogo);
            visita.setData(data);
            attivitaService.saveVisita(visita, organizzatore);
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
        } else {
            visita = new Visita();
            visita.setTitolo(titolo);
            visita.setDescrizione(descrizione);
            visita.setLuogo(luogo);
            visita.setData(data);
            attivitaService.saveVisita(visita, organizzatore);
            return new ResponseEntity<>("Visita creata", HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/elimina/{id}")
    public ResponseEntity<Object> eliminaAttivita(@PathVariable int id) {
        attivitaService.deleteVisita(id);
        return new ResponseEntity<>("Attivita eliminata con successo", HttpStatus.OK);
    }

    @PutMapping("/visite/aggiorna")
    public ResponseEntity<Object> aggiornaVisita(@RequestBody Visita visitaAggiornata) {
        if (attivitaService.existsVisita(visitaAggiornata.getId())) {
            Users organizzatore = userService.getCurrentUser();
            attivitaService.saveVisita(visitaAggiornata, organizzatore);
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

    @GetMapping("/{id}/prenotazioni")
    public ResponseEntity<Object> getPrenotazioniById(@PathVariable int id) {
        if (attivitaService.existsAttivita(id)) {
            Visita visita = attivitaService.getVisitaOEventoById(id).get();
            Set<Users> prenotati = visita.getPrenotazioni();
            return new ResponseEntity<>(prenotati, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/eventi/{id}/invitati")
    public ResponseEntity<Object> getInvitatiById(@PathVariable int id) {
        if (attivitaService.existsEvento(id)) {
            Evento evento = (Evento) attivitaService.getEventoById(id).get();
            Set<Users> invitati = evento.getInvitati();
            return new ResponseEntity<>(invitati, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/eventi/{id}/invitato")
    public ResponseEntity<Object> aggiungiInvitato(@PathVariable int id, @RequestParam int userId) {
        if (attivitaService.existsEvento(id)) {
            if (userService.existsUser(userId)) {
                Evento evento = (Evento) attivitaService.getEventoById(id).get();
                Users user = userService.getUserById(userId).get();
                if (!evento.getInvitati().contains(user)) {
                    if (userService.isOperatore(user)) {
                        attivitaService.addInvitatoToEvento(evento, user);
                        return new ResponseEntity<>("Operatore " + userId + " Invitato all'Evento " + id, HttpStatus.CREATED);
                    } else
                        return new ResponseEntity<>("Utente " + userId + " Non è un Operatore (Produttore, Trasformatore, Distributore)", HttpStatus.BAD_REQUEST);
                } else
                    return new ResponseEntity<>("Utente " + userId + " è stato già invitato", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Utente " + userId + " Non Trovato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Evento " + id + " Non Trovato", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/eventi/{id}/eliminainvitato")
    public ResponseEntity<Object> eliminaInvitato(@PathVariable int id, @RequestParam int userId) {
        if (attivitaService.existsEvento(id)) {
            if (userService.existsUser(userId)) {
                Evento evento = (Evento) attivitaService.getEventoById(id).get();
                Users user = userService.getUserById(userId).get();
                if (evento.getInvitati().contains(user)) {
                    attivitaService.removeInvitatoFromEvento(evento, user);
                    return new ResponseEntity<>("Operatore " + userId + " eliminato con successo", HttpStatus.OK);
                } else return new ResponseEntity<>("Utente " + userId + " Non Invitato", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Utente " + userId + " Non Trovato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Evento " + id + " Non Trovato", HttpStatus.BAD_REQUEST);
    }
}
