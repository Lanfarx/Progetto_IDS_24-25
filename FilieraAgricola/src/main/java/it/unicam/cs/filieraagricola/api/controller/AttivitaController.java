package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.attivita.Evento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.facades.AttivitaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static it.unicam.cs.filieraagricola.api.commons.utils.ResponseEntityUtil.unauthorizedResponse;

@RestController
@RequestMapping("/attivita")
public class AttivitaController {

    @Autowired
    private AttivitaFacade attivitaFacade;

    @GetMapping("/mie-attivita")
    public ResponseEntity<Object> getMieAttivita() {
        List<Visita> mieAttivita = attivitaFacade.getMieAttivita();
        return new ResponseEntity<>(mieAttivita, HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Object> getAttivita(@PathVariable("id") int id) {
        if (attivitaFacade.existsAttivita(id)) {
            return new ResponseEntity<>(attivitaFacade.getVisitaOEventoById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/visite/aggiungi")
    public ResponseEntity<Object> aggiungiVisita(@RequestBody Visita visita) {
        if (attivitaFacade.existsByParams(visita.getTitolo(), visita.getData(), visita.getLuogo())) {
            return new ResponseEntity<>("La visita già esiste", HttpStatus.BAD_REQUEST);
        }
        attivitaFacade.saveVisita(visita);
        return new ResponseEntity<>("Visita creata", HttpStatus.CREATED);
    }

    private Set<Users> saveAndGetOperatori(Evento evento) {
        Set<Users> invitati = evento.getInvitati();
        Set<Integer> idInvitati = new HashSet<>();
        for (Users invitato : invitati) {
            idInvitati.add(invitato.getId());
        }
        Set<Users> operatori = attivitaFacade.getOperatoriByIds(idInvitati);
        evento.setInvitati(operatori);
        attivitaFacade.saveEvento(evento);
        return operatori;
    }

    @PostMapping("/eventi/aggiungi")
    public ResponseEntity<Object> aggiungiEvento(@RequestBody Evento evento) {
        if (attivitaFacade.existsByParams(evento.getTitolo(), evento.getData(), evento.getLuogo())) {
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

        if (attivitaFacade.existsByParams(titolo, data, luogo)) {
            return new ResponseEntity<>("L'Attivita già esiste", HttpStatus.BAD_REQUEST);
        }
        Visita visita;
        if (idInvitati != null && !idInvitati.isEmpty()) {
            visita = new Evento();
            Set<Users> operatori = new HashSet<>();
            Set<Integer> nonOperatori = new HashSet<>();
            attivitaFacade.processaInvitati(idInvitati, operatori, nonOperatori);
            ((Evento) visita).setInvitati(operatori);
            String responseMessage = "Evento creato";
            if (!nonOperatori.isEmpty()) {
                String nomi = "";
                for (Integer id : nonOperatori) {
                    Users user = attivitaFacade.getUserById(id).get();
                    nomi += user.getUsername() + " ";
                }
                responseMessage += ". Tuttavia, gli utenti: " + nomi + " non sono stati aggiunti perché non sono operatori (Produttore, Trasformatore, Distributore).";
            }
            visita.setTitolo(titolo);
            visita.setDescrizione(descrizione);
            visita.setLuogo(luogo);
            visita.setData(data);
            attivitaFacade.saveVisita(visita);
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
        } else {
            visita = new Visita();
            visita.setTitolo(titolo);
            visita.setDescrizione(descrizione);
            visita.setLuogo(luogo);
            visita.setData(data);
            attivitaFacade.saveVisita(visita);
            return new ResponseEntity<>("Visita creata", HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/elimina/{id}")
    public ResponseEntity<Object> eliminaAttivita(@PathVariable int id) {
        Optional<Visita> attivita = attivitaFacade.getVisitaOEventoById(id);
        if(attivita.isPresent()){
            if (attivitaFacade.isCurrentUserOrganizzatore(attivita.get().getOrganizzatore())) {
                attivitaFacade.deleteVisita(id);
                return new ResponseEntity<>("Attivita eliminata con successo", HttpStatus.OK);
            } else return unauthorizedResponse();
        } else return new ResponseEntity<>("Attivita non trovata", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/visite/aggiorna")
    public ResponseEntity<Object> aggiornaVisita(@RequestBody Visita visitaAggiornata) {
        if(attivitaFacade.existsVisita(visitaAggiornata.getId())){
            Visita visita = attivitaFacade.getVisitaOEventoById(visitaAggiornata.getId()).get();
            if (attivitaFacade.isCurrentUserOrganizzatore(visita.getOrganizzatore())) {
                attivitaFacade.saveVisita(visitaAggiornata);
                return new ResponseEntity<>("Visita " + visitaAggiornata.getId() + " Aggiornata", HttpStatus.OK);
            } else return unauthorizedResponse();
        } else return new ResponseEntity<>("Visita non trovata", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/eventi/aggiorna")
    public ResponseEntity<Object> aggiornaEvento(@RequestBody Evento eventoAggiornato) {
        if (attivitaFacade.existsEvento(eventoAggiornato.getId())) {
            Evento evento = (Evento) attivitaFacade.getVisitaOEventoById(eventoAggiornato.getId()).get();
            if(attivitaFacade.isCurrentUserOrganizzatore(evento.getOrganizzatore())) {
                Set<Users> operatori = saveAndGetOperatori(eventoAggiornato);
                return new ResponseEntity<>("Evento " + eventoAggiornato.getId() + " Aggiornato con " + operatori.size() + " invitati", HttpStatus.OK);
            } else return unauthorizedResponse();
        } else return ResponseEntity.status(404).body("Evento " + eventoAggiornato.getId() + " Non Trovato");
    }

    @GetMapping("/{id}/prenotazioni")
    public ResponseEntity<Object> getPrenotazioniById(@PathVariable int id) {
        if (attivitaFacade.existsAttivita(id)) {
            Visita visita = attivitaFacade.getVisitaOEventoById(id).get();
            if(attivitaFacade.isCurrentUserOrganizzatore(visita.getOrganizzatore())) {
                Set<Users> prenotati = visita.getPrenotazioni();
                return new ResponseEntity<>(prenotati, HttpStatus.OK);
            } else return unauthorizedResponse();
        } else return new ResponseEntity<>("Attivita non trovata", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/eventi/{id}/invitati")
    public ResponseEntity<Object> getInvitatiById(@PathVariable int id) {
        if (attivitaFacade.existsEvento(id)) {
            Evento evento = (Evento) attivitaFacade.getEventoById(id).get();
            if(attivitaFacade.isCurrentUserOrganizzatore(evento.getOrganizzatore())) {
                Set<Users> invitati = evento.getInvitati();
                return new ResponseEntity<>(invitati, HttpStatus.OK);
            } else return unauthorizedResponse();
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/eventi/{id}/invitato")
    public ResponseEntity<Object> aggiungiInvitato(@PathVariable int id, @RequestParam int userId) {
        if (attivitaFacade.existsEvento(id)) {
            if (attivitaFacade.existsUser(userId)) {
                Evento evento = (Evento) attivitaFacade.getEventoById(id).get();
                if (attivitaFacade.isCurrentUserOrganizzatore(evento.getOrganizzatore())) { //Is user org
                    if (!attivitaFacade.isUserInInvitati(evento.getInvitati(), userId)) { //is currentUserInInvitati
                        if (attivitaFacade.isUserOperatore(userId)) { //is currentuUser operatore
                            attivitaFacade.addInvitatoToEvento(evento, userId);
                            return new ResponseEntity<>("Operatore " + userId + " Invitato all'Evento " + id, HttpStatus.CREATED);
                        } else return new ResponseEntity<>("Utente " + userId + " Non è un Operatore (Produttore, Trasformatore, Distributore)", HttpStatus.BAD_REQUEST);
                    } else return new ResponseEntity<>("Utente " + userId + " è stato già invitato", HttpStatus.BAD_REQUEST);
                } else return unauthorizedResponse();
            } else return new ResponseEntity<>("Utente " + userId + " Non Trovato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Evento " + id + " Non Trovato", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/eventi/{id}/eliminainvitato")
    public ResponseEntity<Object> eliminaInvitato(@PathVariable int id, @RequestParam int userId) {
        if (attivitaFacade.existsEvento(id)) {
            if (attivitaFacade.existsUser(userId)) {
                Evento evento = (Evento) attivitaFacade.getEventoById(id).get();
                if (attivitaFacade.isCurrentUserOrganizzatore(evento.getOrganizzatore())) {
                    if (attivitaFacade.isUserInInvitati(evento.getInvitati(), userId)) {
                        attivitaFacade.removeInvitatoFromEvento(evento, userId);
                        return new ResponseEntity<>("Operatore " + userId + " eliminato con successo dall' evento: " + id, HttpStatus.OK);
                    } else return new ResponseEntity<>("Utente " + userId + " Non Invitato", HttpStatus.BAD_REQUEST);
                } else return unauthorizedResponse();
            } else return new ResponseEntity<>("Utente " + userId + " Non Trovato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Evento " + id + " Non Trovato", HttpStatus.BAD_REQUEST);
    }
}
