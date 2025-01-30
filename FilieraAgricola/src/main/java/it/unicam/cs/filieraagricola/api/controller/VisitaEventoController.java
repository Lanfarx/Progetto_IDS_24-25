package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Evento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.Visita;
import it.unicam.cs.filieraagricola.api.repository.UserRepository;
import it.unicam.cs.filieraagricola.api.repository.VisitaRepository;
import it.unicam.cs.filieraagricola.api.services.VisitaEventoService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/animatore")
public class VisitaEventoController {

    private final VisitaRepository visitaRepository;
    private final UserRepository userRepository;
    private final VisitaEventoService  visitaEventoService;

    public VisitaEventoController(VisitaRepository visitaRepository, UserRepository userRepository, VisitaEventoService visitaEventoService) {
        this.visitaRepository = visitaRepository;
        this.userRepository = userRepository;
        this.visitaEventoService = visitaEventoService;
    }

    @PostConstruct
    public void initSampleData() {
        Visita visita = new Visita();
        visita.setTitolo("ProvaVisita");
        visita.setDescrizione("questa è una visita");
        visita.setLuogo("Genzano");
        visita.setData(LocalDate.ofEpochDay(1 / 2003));

        Users user1 = new Users();
        user1.setUsername("prenotato");
        user1.setPassword("prenotato");
        user1.getRoles().add(UserRole.ACQUIRENTE);
        userRepository.save(user1);
        visita.getPrenotazioni().add(user1);

        visitaRepository.save(visita);

        Evento evento = new Evento();
        evento.setTitolo("Baudo");
        evento.setDescrizione("questo è un evento");
        evento.setLuogo("Civitanova");
        evento.setData(LocalDate.ofEpochDay(26 / 06 / 2003));

        Users user2 = new Users();
        user2.setUsername("invitato");
        user2.setPassword("invitato");
        userRepository.save(user2);

        evento.getPrenotazioni().add(user1);
        evento.getInvitati().add(user2);

        visitaRepository.save(evento);
    }

    @GetMapping("/visite")
    public ResponseEntity<Object> getVisite() {
        return new ResponseEntity<>(visitaRepository.findAllVisite(), HttpStatus.OK);
    }

    @GetMapping("/eventi")
    public ResponseEntity<Object> getEventi() {
        return new ResponseEntity<>(visitaRepository.findAllEventi(), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Object> getVisitaOEvento(@PathVariable("id") int id) {
        if (visitaRepository.existsById(id)) {
            return new ResponseEntity<>(visitaRepository.findById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping({"/aggiungi"})
    public ResponseEntity<Object> aggiungiVisitaOEvento(@RequestBody Visita visita) {
        if (this.visitaRepository.existsByTitoloAndDataAndDescrizioneAndLuogo(visita.getTitolo(), visita.getData(), visita.getDescrizione(), visita.getLuogo())) {
            return new ResponseEntity<>("L'attivita gia esiste", HttpStatus.BAD_REQUEST);
        } else {
            this.visitaRepository.save(visita);
            return new ResponseEntity<>("Attivita creata", HttpStatus.CREATED);
        }
    }

    @PostMapping("/aggiungiconparametri")
    public ResponseEntity<Object> aggiungiVisitaOEventoConParametri(
            @RequestParam String titolo,
            @RequestParam String descrizione,
            @RequestParam String luogo,
            @RequestParam LocalDate data,
            @RequestParam(required = false) Set<Integer> idInvitati) {

        if (visitaRepository.existsByTitoloAndDataAndDescrizioneAndLuogo(
                titolo, data, descrizione, luogo)) {
            return new ResponseEntity<>("L'Attivita già esiste", HttpStatus.BAD_REQUEST  );
        } else {
            Visita visita;
            if (idInvitati != null && !idInvitati.isEmpty()) {
                visita = new Evento();
                Set<Users> operatori = new HashSet<>();
                Set<Integer> nonOperatori = new HashSet<>();

                for (Integer id : idInvitati) {
                    if (this.userRepository.existsById(id)) {
                        Users user = userRepository.findById(id).get();
                        if (visitaEventoService.isOperatore(user)) {
                            operatori.add(user);
                        } else {
                            nonOperatori.add(id);
                        }}
                }
                ((Evento) visita).setInvitati(operatori);

                String responseMessage = "Evento creato";
                if (!nonOperatori.isEmpty()) {
                    responseMessage += ". Tuttavia, gli utenti con ID " + nonOperatori + " non sono stati aggiunti perché non hanno i ruoli richiesti.";
                }
                visita.setTitolo(titolo);
                visita.setDescrizione(descrizione);
                visita.setLuogo(luogo);
                visita.setData(data);
                visitaRepository.save(visita);
                return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
            } else {
                visita = new Visita();
                visita.setTitolo(titolo);
                visita.setDescrizione(descrizione);
                visita.setLuogo(luogo);
                visita.setData(data);
                visitaRepository.save(visita);
                return new ResponseEntity<>("Visita creata", HttpStatus.CREATED);
            }
        }
    }

    @DeleteMapping("/elimina/{id}")
    public ResponseEntity<Object> eliminaVisitaOEvento(@PathVariable int id) {
        visitaRepository.deleteById(id);
        return new ResponseEntity<>("Attivita eliminata con successo", HttpStatus.OK);
    }

    @PutMapping("/aggiorna")
    public ResponseEntity<Object> aggiornaVisitaOEvento(@RequestBody Visita visitaAggiornata) {
        if (this.visitaRepository.existsById(visitaAggiornata.getId())) {
            this.visitaRepository.save(visitaAggiornata);
            return new ResponseEntity<>("Attivita " + visitaAggiornata.getId() + " Aggiornata", HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("Attivita " + visitaAggiornata.getId() + " Non Trovata");
        }
    }

    public boolean aggiungiPrenotazione(Visita visita, Users user) {
        if(visitaRepository.existsByTitoloAndDataAndDescrizioneAndLuogo(
                visita.getTitolo(), visita.getData(), visita.getDescrizione(), visita.getLuogo())) {
            visita.getPrenotazioni().add(user);
            return true;
        }
        return false;
    }

    public boolean eliminaPrenotazione(Visita visita, Users user){
        if(visitaRepository.existsByTitoloAndDataAndDescrizioneAndLuogo(
                visita.getTitolo(), visita.getData(), visita.getDescrizione(), visita.getLuogo())) {
            Set<Users> prenotazioni = visita.getPrenotazioni();
            prenotazioni.remove(user);
            return true;
        }
        return false;
    }

    @PostMapping("/evento/{id}/invitato")
    public ResponseEntity<Object> aggiungiInvitato(@PathVariable int id, @RequestParam int userId) {
        if (this.visitaRepository.existsById(id)) {
            if (this.userRepository.existsById(userId)) {
                Evento evento = (Evento) this.visitaRepository.findEventoById(id).get();
                Users user = this.userRepository.findById(userId).get();
                if (visitaEventoService.isOperatore(user)) {
                    evento.getInvitati().add(user);
                    this.visitaRepository.save(evento);
                    return new ResponseEntity<>("Operatore " + userId + " Invitato all'Evento " + id, HttpStatus.CREATED);
                } else return new ResponseEntity<>("Utente " + userId + " Non è un Operatore (Produttore, Trasformatore, Distributore)", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Utente " + userId + " Non Trovato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Evento " + id + " Non Trovato", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/evento/{id}/eliminainvitato")
    public ResponseEntity<Object> eliminaInvitato(@PathVariable int id, @RequestParam int userId) {
        if (this.visitaRepository.existsById(id)) {
            if (this.userRepository.existsById(userId)) {
                Evento evento = (Evento) this.visitaRepository.findEventoById(id).get();
                Users user = this.userRepository.findById(userId).get();
                if (visitaEventoService.isOperatore(user)) {
                    evento.getInvitati().remove(user);
                    this.visitaRepository.save(evento);
                    return new ResponseEntity<>("Operatore " + userId + " Eliminato dall'Evento " + id, HttpStatus.CREATED);
                } else return new ResponseEntity<>("Utente " + userId + " Non è un Operatore (Produttore, Trasformatore, Distributore)", HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>("Utente " + userId + " Non Trovato", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("Evento " + id + " Non Trovato", HttpStatus.BAD_REQUEST);
    }
}
