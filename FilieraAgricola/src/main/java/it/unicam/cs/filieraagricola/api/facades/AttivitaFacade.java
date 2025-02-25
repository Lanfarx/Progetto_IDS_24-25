package it.unicam.cs.filieraagricola.api.facades;


import it.unicam.cs.filieraagricola.api.entities.attivita.Evento;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.services.AttivitaService;
import it.unicam.cs.filieraagricola.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class AttivitaFacade {

    @Autowired
    private AttivitaService attivitaService;

    @Autowired
    private UserService userService;


    public List<Visita> getMieAttivita() {
        Users organizzatore = userService.getCurrentUser();
        return attivitaService.getAttivitaByOrganizzatore(organizzatore);
    }
    
    public boolean existsAttivita(int id){
        return attivitaService.existsAttivita(id);
    }

    public boolean existsByParams(String titolo, LocalDate data, String luogo){
        return attivitaService.existsByParams(titolo, data, luogo);
    }

    public boolean existsVisita(int id){
        return attivitaService.existsVisita(id);
    }

    public boolean existsEvento(int id){
        return attivitaService.existsEvento(id);
    }

    public void saveVisita(Visita visita){
        Users organizzatore = userService.getCurrentUser();
        attivitaService.saveVisita(visita, organizzatore);
    }

    public void saveEvento(Evento evento){
        Users organizzatore = userService.getCurrentUser();
        attivitaService.saveEvento(evento, organizzatore);
    }

    public void deleteVisita(int id){
        attivitaService.deleteVisita(id);
    }

    public Optional<Visita> getEventoById(int id){
        return attivitaService.getEventoById(id);
    }

    public void addInvitatoToEvento(Evento evento, int userId){
        Users user = userService.getUserById(userId).get();
        attivitaService.addInvitatoToEvento(evento, user);
    }

    public void removeInvitatoFromEvento(Evento evento, int userId){
        Users user = userService.getUserById(userId).get();
        attivitaService.removeInvitatoFromEvento(evento, user);
    }

    public boolean checkData(LocalDate data){
        return attivitaService.checkData(data);
    }

    public Optional<Visita> getVisitaOEventoById(int id) {
        return attivitaService.getVisitaOEventoById(id);
    }

    //User per le attività

    public Set<Users> getOperatoriByIds(Set<Integer> idInvitati){
        return userService.getOperatoriByIds(idInvitati);
    }

    public void processaInvitati(Set<Integer> idInvitati, Set<Users> operatori, Set<Integer> nonOperatori){
        userService.processaInvitati(idInvitati, operatori, nonOperatori);
    }

    public Optional<Users> getUserById(int id){
        return userService.getUserById(id);
    }

    public boolean isCurrentUserOrganizzatore(Users user){
        return userService.getCurrentUser() == user;
    }

    public boolean existsUser(int userId){
        return userService.existsUser(userId);
    }

    public boolean isUserInInvitati(Set<Users> invitati, int userId){
        Users user = userService.getUserById(userId).get();
        return invitati.contains(user);
    }

    public boolean isUserOperatore(int userId){
        Users user = userService.getUserById(userId).get();
        return userService.isOperatore(user);
    }
}
