package it.unicam.cs.filieraagricola.api.facades;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaValidazione;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.elemento.ElementoService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaEliminazioneService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaRuoloService;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.RichiestaValidazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RichiestaFacade {

    @Autowired
    private RichiestaEliminazioneService richiestaEliminazioneService;

    @Autowired
    private RichiestaRuoloService richiestaRuoloService;

    @Autowired
    private RichiestaValidazioneService richiestaValidazioneService;

    @Autowired
    private UserService userService;

    @Autowired
    private ElementoService elementoService;


    //RICHIESTA ELIMINAZIONE
    public List<Richiesta> getRichiesteEliminazioneByUser(Users user) {
        return richiestaEliminazioneService.getRichiesteByUser(user);
    }

    public void aggiungiRichiestaEliminazione(String motivazione) {
        Users user = userService.getCurrentUser();
        richiestaEliminazioneService.aggiungiRichiesta(user, motivazione);
    }

    public boolean existsRichiestaEliminazione(Integer id) {
        return richiestaEliminazioneService.existsRichiesta(id);
    }

    public boolean existsSameRichiestaEliminazione(String motivazione) {
        Users user = userService.getCurrentUser();
        return richiestaEliminazioneService.existsSameRichiesta(user, motivazione);
    }

    public Optional<RichiestaEliminazione> getRichiestaEliminazione(Integer id) {
        return richiestaEliminazioneService.getRichiesta(id);
    }

    public List<RichiestaEliminazione> getRichiesteEliminazioneInAttesa() {
        return richiestaEliminazioneService.getRichiesteInAttesa();
    }

    public List<RichiestaEliminazione> getMieRichiesteEliminazione(){
        Users currentUser = userService.getCurrentUser();
        return richiestaEliminazioneService.getMieRichiesteEliminazione(currentUser);
    }

    //RICHIESTA RUOLO
    public List<Richiesta> getRichiesteRuoloByUser(Users user) {
        return richiestaRuoloService.getRichiesteByUser(user);
    }

    public boolean currentUserAlreadyHasRuolo(UserRole ruolo){
        Users currentUser = userService.getCurrentUser();
        return richiestaRuoloService.userAlreadyHasRuolo(currentUser, ruolo);
    }

    public void aggiungiRichiestaRuolo(UserRole ruoloRichiesto) {
        Users user = userService.getCurrentUser();
        richiestaRuoloService.aggiungiRichiesta(user, ruoloRichiesto);
    }

    public boolean existsRichiestaRuolo(Integer id) {
        return richiestaRuoloService.existsRichiesta(id);
    }

    public boolean existsSameRichiestaRuolo(UserRole ruolo) {
        Users user = userService.getCurrentUser();
        return richiestaRuoloService.existsSameRichiesta(user, ruolo);
    }

    public Optional<RichiestaRuolo> getRichiestaRuolo(Integer id) {
        return richiestaRuoloService.getRichiesta(id);
    }

    public List<RichiestaRuolo> getRichiesteRuoloInAttesa() {
        return richiestaRuoloService.getRichiesteInAttesa();
    }

    public List<RichiestaRuolo> getMieRichiesteRuolo(){
        Users currentUser = userService.getCurrentUser();
        return richiestaRuoloService.getMieRichiesteRuolo(currentUser);
    }


    // RICHIESTA VALIDAZIONE
    public List<Richiesta> getRichiesteValidazioneByUser(Users user) {
        return richiestaValidazioneService.getRichiesteByUser(user);
    }

    public boolean isUserCurrentUser(Users user) {
        Users currentUser = userService.getCurrentUser();
        return currentUser.equals(user);
    }

    public void aggiungiRichiestaValidazione(Object elemento) {
        Users user = userService.getCurrentUser();
        richiestaValidazioneService.aggiungiRichiesta(user, elemento);
    }

    public boolean existsRichiestaValidazione(Integer id) {
        return richiestaValidazioneService.existsRichiesta(id);
    }

    public boolean existsSameRichiestaValidazione(Object elemento) {
        Users user = userService.getCurrentUser();
        return richiestaValidazioneService.existsSameRichiesta(user, elemento);
    }

    public Optional<RichiestaValidazione> getRichiestaValidazione(Integer id) {
        return richiestaValidazioneService.getRichiesta(id);
    }

    public List<RichiestaValidazione> getRichiesteValidazioneInAttesa() {
        return richiestaValidazioneService.getRichiesteInAttesa();
    }

    public void processaRichiestaEliminazione(Integer richiestaId, boolean approvato) {
        richiestaEliminazioneService.processaRichiesta(richiestaId, approvato);
    }

    public void processaRichiestaRuolo(Integer richiestaId, boolean approvato) {
        richiestaRuoloService.processaRichiesta(richiestaId, approvato);
    }

    public void processaRichiestaValidazione(Integer richiestaId, boolean approvato) {
        richiestaValidazioneService.processaRichiesta(richiestaId, approvato);
    }

    public List<RichiestaValidazione> getMieRichiesteValidazione() {
        Users currentUser = userService.getCurrentUser();
        return richiestaValidazioneService.getMieRichiesteValidazione(currentUser);
    }

    //Elemento service per le richieste
    public boolean existsElementoAndAttesa(int id){
        return elementoService.existsElementoAndAttesa(id);
    }

    public Optional<Elemento> getElemento(int id) {
        return elementoService.getElemento(id);
    }
}