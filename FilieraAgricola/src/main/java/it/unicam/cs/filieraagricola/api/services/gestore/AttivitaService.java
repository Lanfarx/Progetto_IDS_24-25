package it.unicam.cs.filieraagricola.api.services.gestore;

import it.unicam.cs.filieraagricola.api.entities.attivita.Evento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.repository.AttivitaRepository;
import it.unicam.cs.filieraagricola.api.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AttivitaService {

    @Autowired
    private final AttivitaRepository attivitaRepository;

    public AttivitaService(AttivitaRepository attivitaRepository) {
        this.attivitaRepository = attivitaRepository;
    }

    public List<Visita> getAllAttivita() {
        return attivitaRepository.findAll();
    }

    public List<Visita> getAllVisite() {
        return attivitaRepository.findAllVisite();
    }

    public List<Visita> getAllEventi() {
        return attivitaRepository.findAllEventi();
    }

    public Optional<Visita> getVisitaOEventoById(int id) {
        return attivitaRepository.findById(id);
    }

    public List<Visita> getAttivitaByOrganizzatore(Users organizzatore) {
        return attivitaRepository.findByOrganizzatore(organizzatore);
    }

    public boolean existsAttivita(int id) {
        return attivitaRepository.existsById(id);
    }

    public boolean existsVisitaByParams(String titolo, LocalDate data, String descrizione, String luogo) {
        return attivitaRepository.existsByTitoloAndDataAndDescrizioneAndLuogo(titolo, data, descrizione, luogo);
    }

    public boolean existsVisita(int id) {
        return attivitaRepository.existsVisitaById(id);
    }

    public boolean existsEvento(int id) {
        return attivitaRepository.existsEventoById(id);
    }

    public void saveVisita(Visita visita, Users organizzatore) {
        visita.setOrganizzatore(organizzatore);
        attivitaRepository.save(visita);
    }

    public void saveEvento(Evento evento, Users organizzatore) {
        evento.setOrganizzatore(organizzatore);
        attivitaRepository.save(evento);
    }

    public void deleteVisita(int id) {
        attivitaRepository.deleteById(id);
    }

    public Optional<Visita> getEventoById(int id) {
        return attivitaRepository.findEventoById(id);
    }

    public void addInvitatoToEvento(Evento evento, Users user) {
        evento.getInvitati().add(user);
        attivitaRepository.save(evento);
    }

    public void removeInvitatoFromEvento(Evento evento, Users user) {
        evento.getInvitati().remove(user);
        attivitaRepository.save(evento);
    }

    public boolean aggiungiPrenotazione(Visita visita, Users user) {
        if (existsVisitaByParams(visita.getTitolo(), visita.getData(), visita.getDescrizione(), visita.getLuogo())) {
            if (visita.getData().isAfter(LocalDate.now())) {
                visita.getPrenotazioni().add(user);
                attivitaRepository.save(visita);
                return true;
            } else return false;
        }
        return false;
    }

    public boolean eliminaPrenotazione(Visita visita, Users user) {
        if (existsVisitaByParams(visita.getTitolo(), visita.getData(), visita.getDescrizione(), visita.getLuogo())) {
            visita.getPrenotazioni().remove(user);
            attivitaRepository.save(visita);
            return true;
        }
        return false;
    }

    public void eliminaAllPrenotazione(Users user) {
        List<Visita> visitePrenotate = attivitaRepository.findAllByPrenotazioniContains(user);

        for (Visita visita : visitePrenotate) {
            visita.getPrenotazioni().remove(user);
        }

        attivitaRepository.saveAll(visitePrenotate);
    }
}
