package it.unicam.cs.filieraagricola.api.facades;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.ElementoCarrello;
import it.unicam.cs.filieraagricola.api.entities.carrello.ElementoOrdine;
import it.unicam.cs.filieraagricola.api.entities.carrello.Ordine;
import it.unicam.cs.filieraagricola.api.entities.elemento.Elemento;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.carrello.CarrelloService;
import it.unicam.cs.filieraagricola.api.services.carrello.OrdineService;
import it.unicam.cs.filieraagricola.api.services.elemento.ElementoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarrelloFacade <T extends Elemento>{

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private OrdineService ordineService;

    @Autowired
    private UserService userService;

    @Autowired
    private ElementoService elementoService;


    public Carrello getCarrello() {
        Users currentUser = userService.getCurrentUser();
        return carrelloService.getCarrello(currentUser);
    }

    public void aggiungiAlCarrello(Elemento elemento, int quantita) {
        Users currentUser = userService.getCurrentUser();
        carrelloService.aggiungiAlCarrello(currentUser, elemento, quantita);
    }

    public void rimuoviDalCarrello(Elemento elemento, int quantita) {
        Users currentUser = userService.getCurrentUser();
        carrelloService.rimuoviDalCarrello(currentUser, elemento, quantita);
    }

    public void rimuoviDaCarrelli(int elementoId) {
        carrelloService.rimuoviDaCarrelli(elementoId);
    }

    public boolean contieneElemento(Elemento elemento) {
        Users currentUser = userService.getCurrentUser();
        Carrello carrello = carrelloService.getCarrello(currentUser);
        return carrelloService.contieneElemento(carrello, elemento);
    }

    public Ordine creaOrdine() {
        Users currentUser = userService.getCurrentUser();
        Carrello carrello = carrelloService.getCarrello(currentUser);
        return ordineService.creaOrdine(carrello);
    }

    public List<Ordine> getOrdini() {
        Users currentUser = userService.getCurrentUser();
        return ordineService.getOrdini(currentUser);
    }

    public void pulisciCarrello() {
        Users currentUser = userService.getCurrentUser();
        Carrello carrello = carrelloService.getCarrello(currentUser);
        ordineService.creaOrdine(carrello);
    }

    public List<ElementoCarrello> getElementiCarrello() {
        Users currentUser = userService.getCurrentUser();
        return carrelloService.getCarrello(currentUser).getElementi();
    }

    public List<ElementoOrdine> getElementiOrdine(Ordine ordine) {
        return ordine.getElementi();
    }


    //ELEMENTO PER CARRELLO

    public List<T> getElementiValidi(){
        return elementoService.getElementiValidi();
    }

    public Optional<T> getElemento(int id){
        return elementoService.getElemento(id);
    }

    public boolean existsElemento(int id){
        return elementoService.existsElemento(id);
    }

    public void eliminaElemento(int id){
        elementoService.eliminaElemento(id);
    }

    public boolean checkDisponibilita(Elemento elemento, int quantita) {
        return elementoService.checkDisponibilita(elemento, quantita);
    }

    public boolean existsElementoAndValido(int id) {
        return elementoService.existsElementoAndValido(id);
    }

    public boolean existsElementoAndAttesa(Integer id) {
        return elementoService.existsElementoAndAttesa(id);
    }


    }
