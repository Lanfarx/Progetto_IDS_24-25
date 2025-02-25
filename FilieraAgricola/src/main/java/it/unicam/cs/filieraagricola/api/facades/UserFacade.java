package it.unicam.cs.filieraagricola.api.facades;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.entities.carrello.Ordine;
import it.unicam.cs.filieraagricola.api.entities.elemento.*;
import it.unicam.cs.filieraagricola.api.entities.richieste.Richiesta;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaEliminazione;
import it.unicam.cs.filieraagricola.api.entities.richieste.RichiestaRuolo;
import it.unicam.cs.filieraagricola.api.services.*;
import it.unicam.cs.filieraagricola.api.services.carrello.OrdineService;
import it.unicam.cs.filieraagricola.api.services.elemento.*;
import it.unicam.cs.filieraagricola.api.services.gestore.richieste.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class UserFacade {

    @Autowired
    private UserService userService;
    @Autowired
    private AttivitaService attivitaService;
    @Autowired
    private ElementoService<Elemento> elementoService;
    @Autowired
    private ProdottoService<Prodotto> prodottoService;
    @Autowired
    private PacchettoService pacchettoService;
    @Autowired
    private ProdottoBaseService prodottoBaseService;
    @Autowired
    private ProdottoTrasformatoService prodottoTrasformatoService;
    @Autowired
    private RichiestaEliminazioneService richiestaEliminazioneService;
    @Autowired
    private RichiestaRuoloService richiestaRuoloService;
    @Autowired
    private OrdineService ordineService;

    // Gestione Utente
    public void registerUser(String username, String password){
        userService.registerUser(username, password);
    }

    public Users getCurrentUser() {
        return userService.getCurrentUser();
    }

    public Optional<Users> getUserById(int id) {
        return userService.getUserById(id);
    }

    public Optional<Users> findByUsername(String username){
        return userService.findByUsername(username);
    }

    public boolean verifyPassword(String password, String userPassword){
        return userService.verifyPassword(password, userPassword);
    }

    public void modificaCredenziali(Users user, String username, String password) {
        userService.modificaCredenziali(user, username, password);
    }

    public boolean existsUserByUsername(String username) {
        return userService.existsUserByUsername(username);
    }

    public boolean existsUser(int id) {
        return userService.existsUser(id);
    }

    public void rimuoviRuolo(int userID, UserRole role){
        userService.rimuoviRuolo(userID, role);
    }

    public void aggiungiRuolo(int userID, UserRole role){
        userService.aggiungiRuolo(userID, role);
    }

    public void deleteUser(Users user){
        userService.delete(user);
    }


    // Attività e Prenotazioni
    public List<?> getAllAttivita() {
        return attivitaService.getAllAttivita();
    }

    public List<?> getAllVisite() {
        return attivitaService.getAllVisite();
    }

    public List<?> getAllEventi() {
        return attivitaService.getAllEventi();
    }

    public List<?> getAllPrenotazioni() {
        return attivitaService.getAllPrenotazioni(userService.getCurrentUser());
    }

    public boolean existsAttivita(int id) {
        return attivitaService.existsAttivita(id);
    }

    public Optional<Visita> getVisitaOEventoById(int id) {
        return attivitaService.getVisitaOEventoById(id);
    }

    public boolean controllaPrenotazione(Visita visita, Users user) {
        return attivitaService.controllaPrenotazione(visita, user);
    }

    public boolean aggiungiPrenotazione(Visita visita, Users user) {
        return attivitaService.aggiungiPrenotazione(visita, user);
    }

    public boolean checkData(LocalDate data){
        return attivitaService.checkData(data);
    }

    public boolean eliminaPrenotazione(Visita visita, Users user) {
        return attivitaService.eliminaPrenotazione(visita, user);
    }


    // Elementi e Prodotti
    public List<Elemento> getElementiValidi() {
        return elementoService.getElementiValidi();
    }

    public List<Prodotto> getProdottiValidi() {
        return prodottoService.getProdottiValidi();
    }

    public List<Pacchetto> getPacchettiValidi() {
        return pacchettoService.getAllPacchettiValidi();
    }


    public List<ProdottoBase> getProdottiBaseValidi() {
        return prodottoBaseService.getAllProdottiBaseValidi();
    }

    public List<ProdottoTrasformato> getProdottiTrasformatiValidi() {
        return prodottoTrasformatoService.getAllProdottiTrasformatiValidi();
    }

    // Gestione richieste
    public List<Richiesta> getRichiesteByUser(Users user) {
        return richiestaRuoloService.getRichiesteByUser(user);
    }

    public List<RichiestaRuolo> getRichiesteRuolo(Users user) {
        return richiestaRuoloService.getMieRichiesteRuolo(user);
    }

    public List<RichiestaEliminazione> getRichiesteEliminazione(Users user) {
        return richiestaEliminazioneService.getMieRichiesteEliminazione(user);
    }

    // Gestione ordini
    public List<Ordine> getOrdini(){
        Users currentUser = getCurrentUser();
        return ordineService.getOrdini(currentUser);
    }
}
