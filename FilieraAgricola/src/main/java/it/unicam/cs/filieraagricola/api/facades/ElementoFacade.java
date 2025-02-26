package it.unicam.cs.filieraagricola.api.facades;

import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.*;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.carrello.CarrelloService;
import it.unicam.cs.filieraagricola.api.services.elemento.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class ElementoFacade<T extends Prodotto> {

    //Services necessarie al funzionamento del facade
    @Autowired
    private ElementoService elementoService;
    @Autowired
    private PacchettoService pacchettoService;
    @Autowired
    private ProdottoBaseService prodottoBaseService;
    @Autowired
    private ProdottoTrasformatoService prodottoTrasformatoService;
    @Autowired
    private ProdottoService prodottoService;
    @Autowired
    protected CategoriaService categoriaService;
    @Autowired
    protected CarrelloService carrelloService;
    @Autowired
    @Lazy
    UserService userService;


    // Operazioni generiche per tutti gli elementi
    public List<Elemento> getAllElementi() {
        return elementoService.getElementiValidi();
    }

    public Optional<Elemento> getElementoById(int id) {
        return elementoService.getElemento(id);
    }

    public void deleteElemento(int id) {
        elementoService.eliminaElemento(id);
    }


    // Operazioni specifiche per Pacchetto

    public boolean existsPacchetto(int id) {
       return pacchettoService.existsPacchetto(id);
    }
    public List<Pacchetto> getAllPacchetti() {
        return pacchettoService.getPacchetti(userService.getCurrentUser());
    }

    public List<Pacchetto> getAllPacchettiValidi() {
        return pacchettoService.getAllPacchettiValidi();
    }

    public Pacchetto getPacchettoById(int id) {
        return pacchettoService.getPacchetto(id);
    }

    public boolean aggiungiPacchetto(Pacchetto pacchetto) {
        Categoria categoria = categoriaService.getCategoriaByNome("Pacchetto").get();
        Users operatore = userService.getCurrentUser();
        return pacchettoService.aggiungiPacchetto(pacchetto, operatore, categoria);
    }

    public boolean aggiungiPacchettoConParametri(String nome, String descrizione, double prezzo, Set<Integer> idProdottiSet){
        Categoria categoria = categoriaService.getCategoriaByNome("Pacchetto").get();
        Users operatore = userService.getCurrentUser();
        return pacchettoService.aggiungiPacchettoConParametri(nome, descrizione, prezzo, idProdottiSet, operatore, categoria);
    }

    public boolean aggiungiProdotto(int idPacchetto, int idProdotto){
        return pacchettoService.aggiungiProdotto(idPacchetto, idProdotto);
    }

    public void eliminaPacchetto(int id) {
        pacchettoService.eliminaPacchetto(id);
    }

    public boolean eliminaProdotto(int idPacchetto, int idProdotto) {
        return pacchettoService.eliminaProdotto(idPacchetto, idProdotto);
    }

    public boolean aggiornaPacchetto(Pacchetto pacchetto) {
        return pacchettoService.aggiornaPacchetto(pacchetto);
    }



    // Operazioni specifiche per ProdottoBase
    public List<ProdottoBase> getAllProdottiBase() {
        return prodottoBaseService.getProdottiBase(userService.getCurrentUser());
    }

    public ProdottoBase getProdottoBaseById(int id) {
        return prodottoBaseService.getProdottoBase(id);
    }

    public boolean aggiungiProdottoBase(String nome, String metodiColtivazione,
                                        String certificazioni, String descrizione,
                                        double prezzo, Categoria categoria) {
        return prodottoBaseService.aggiungiProdottoBase(nome, metodiColtivazione,
                certificazioni, descrizione, prezzo, categoria, userService.getCurrentUser());
    }

    public boolean aggiungiProdottoBase(ProdottoBase prodottoBase) {
        //get perché la condizione di esistenza della categoria è verificata nel controller.

        Categoria categoria = categoriaService.getCategoriaByNome(prodottoBase.getCategoria().getNome()).get();

        return prodottoBaseService.aggiungiProdottoBase(prodottoBase, userService.getCurrentUser(), categoria);
    }

    public List<ProdottoBase> getAllProdottiBaseValidi() {
        return prodottoBaseService.getAllProdottiBaseValidi();
    }

    public List<ProdottoBase> getAllProdottiBaseByUser(Users user){
        return prodottoBaseService.getAllProdottiBaseByUser(user);
    }

    public void eliminaProdottoBase(int id) {
        Set<Pacchetto> pacchettoSet = pacchettoService.getPacchettiConProdotto(id);
        carrelloService.rimuoviDaCarrelli(id);
        prodottoBaseService.deleteProdottoBase(id);
        pacchettoService.checkAndDelete(pacchettoSet);
    }

    public boolean aggiornaProdottoBase(ProdottoBase prodottoBase) {
        return prodottoBaseService.aggiornaProdottoBase(prodottoBase, userService.getCurrentUser());
    }

    public boolean existsProdottoBase(int id){
        return prodottoBaseService.existsProdottoBase(id);
    }

    public void deleteProdottoBase(int id) {
        prodottoBaseService.deleteProdottoBase(id);
    }



    // Operazioni specifiche per ProdottoTrasformato

    public List<ProdottoTrasformato> getAllProdottiTrasformati() {
        return prodottoTrasformatoService.getProdottiTrasformati(userService.getCurrentUser());
    }

    public ProdottoTrasformato getProdottoTrasformatoById(int id) {
        return prodottoTrasformatoService.getProdottoTrasformato(id);
    }

    public List<ProdottoTrasformato> getAllProdottiTrasformatiValidi() {
        return prodottoTrasformatoService.getAllProdottiTrasformatiValidi();
    }

    public boolean aggiornaProdottoTrasformato(ProdottoTrasformato prodottoTrasformato) {
        return prodottoTrasformatoService.aggiornaProdottoTrasformato(prodottoTrasformato, userService.getCurrentUser());
    }

    public boolean aggiungiProdottoTrasformato(String nome, String processo,
                                               String certificazioni, int prodottoBaseID,
                                               String descrizione, double prezzo, Categoria categoria) {
        return prodottoTrasformatoService.aggiungiProdottoTrasformato(nome, processo, certificazioni,
                prodottoBaseID, descrizione, prezzo, categoria, userService.getCurrentUser());
    }

    public boolean existsProdottoTrasformato(int id){
        return prodottoTrasformatoService.existsProdottoTrasformato(id);
    }

    public boolean aggiungiProdottoTrasformato(ProdottoTrasformato prodottoTrasformato) {
        Categoria categoria = categoriaService.getCategoriaByNome(prodottoTrasformato.getCategoria().getNome()).get();
        prodottoTrasformato.setCategoria(categoria);
        return prodottoTrasformatoService.aggiungiProdottoTrasformato(prodottoTrasformato, userService.getCurrentUser());
    }

    public void deleteProdottoTrasformato(int id) {
        Set<Pacchetto> pacchettoSet = pacchettoService.getPacchettiConProdotto(id);
        prodottoTrasformatoService.deleteProdottoTrasformato(id);
        pacchettoService.checkAndDelete(pacchettoSet);
    }


    // Operazioni specifiche per Prodotto

    public boolean aggiornaQuantitaProdotto(int id, int quantita){
        return prodottoService.aggiornaQuantitaProdotto(id, quantita);
    }

    public boolean riduciQuantitaProdotto(int id, int quantita) {
        return prodottoService.riduciQuantitaProdotto(id, quantita);
    }

    public List<T> getAllProdotti() {
        return prodottoService.getProdottiValidi();
    }

    public Prodotto getProdottoById(int id) {
        return prodottoService.getProdottoById(id);
    }

    public boolean existsProdotto(int id) {
        return prodottoService.existsProdotto(id);
    }

    //Operazioni per la categoria sugli elementi
    public List<Categoria> getAllCategorie() {
        return categoriaService.getAllCategorie();
    }

    public Optional<Categoria> getCategoriaByNome(String nomeCategoria) {
        return categoriaService.getCategoriaByNome(nomeCategoria);
    }

    public boolean existsCategoria(Integer idCategoria) {
        return categoriaService.existsCategoria(idCategoria);
    }

    public boolean existsSameCategoria(String nomeCategoria) {
        return categoriaService.existsSameCategoria(nomeCategoria);
    }




    //Operazioni per utenti sull'elemento
    public boolean isUserCurrentUser(Users user) {
        return user == userService.getCurrentUser();
    }
}
