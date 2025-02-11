package it.unicam.cs.filieraagricola.api.controller.elemento;

import it.unicam.cs.filieraagricola.api.commons.utils.PacchettoAggiornatoDTO;
import it.unicam.cs.filieraagricola.api.commons.utils.PacchettoCreatoDTO;
import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Prodotto;
import it.unicam.cs.filieraagricola.api.services.UserService;
import it.unicam.cs.filieraagricola.api.services.elemento.PacchettoService;
import it.unicam.cs.filieraagricola.api.services.elemento.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static it.unicam.cs.filieraagricola.api.commons.utils.ResponseEntityUtil.unauthorizedResponse;

@RestController
@RequestMapping("/operatore/distributore")
public class PacchettoController {

    @Autowired
    private PacchettoService pacchettoService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProdottoService prodottoService;

    @RequestMapping({"/pacchetti"})
    public ResponseEntity<Object> getPacchetti() {
        if (pacchettoService.getPacchetti().isEmpty()) {
            return new ResponseEntity<>("Nessun pacchetto trovato", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pacchettoService.getPacchetti(), HttpStatus.FOUND);
    }

    @RequestMapping({"/pacchetti/{id}"})
    public ResponseEntity<Object> GetPacchetto(@PathVariable("id") int id) {
        if (pacchettoService.getPacchetto(id) == null) {
            return new ResponseEntity<>("Nessun pacchetto trovato", HttpStatus.NOT_FOUND);
        }
        if (pacchettoService.getPacchetto(id).getOperatore() == userService.getCurrentUser()) {
            return new ResponseEntity<>(pacchettoService.getPacchetto(id), HttpStatus.FOUND);
        } else {
            return unauthorizedResponse();
        }
    }

    @PostMapping({"/pacchetti/aggiungi"})
    public ResponseEntity<Object> aggiungiPacchetto(@RequestBody PacchettoCreatoDTO pacchettoCreatoDTO) {
        if (pacchettoCreatoDTO.getProdottiIds().size() < 2) {
            return new ResponseEntity<>("Il pacchetto deve contenere almeno 2 prodotti", HttpStatus.BAD_REQUEST);
        }

        Set<Prodotto> prodotti = new HashSet<>();
        for (Integer id : pacchettoCreatoDTO.getProdottiIds()) {
            Prodotto prodotto = prodottoService.getProdottoById(id);

            if (prodotto == null) {
                return new ResponseEntity<>("Prodotto con ID " + prodotto.getId() + " non esistente", HttpStatus.BAD_REQUEST);
            }
            if (prodotto.getStatorichiesta() != StatoContenuto.ACCETTATA) {
                return new ResponseEntity<>("Tutti i prodotti devono essere stati precedentemente validati per essere aggiunti al pacchetto", HttpStatus.BAD_REQUEST);
            }
            prodotti.add(prodotto);
        }
        Pacchetto pacchetto = new Pacchetto();
        pacchetto.setNome(pacchettoCreatoDTO.getNome());
        pacchetto.setDescrizione(pacchettoCreatoDTO.getDescrizione());
        pacchetto.setProdottiSet(prodotti);
        if (pacchettoService.aggiungiPacchetto(pacchetto)) {
            return new ResponseEntity<>("Pacchetto aggiunto", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Pacchetto già esistente", HttpStatus.CONFLICT);
    }

    @PostMapping({"/pacchetti/aggiungi/prodotto"})
    public ResponseEntity<Object> aggiungiProdotto(@RequestParam("idPacchetto") int idPacchetto,
                                                   @RequestParam("idProdotto") int idProdotto) {
        Prodotto prodotto = prodottoService.getProdottoById(idProdotto);
        if (prodotto == null) {
            return new ResponseEntity<>("Prodotto non trovato", HttpStatus.NOT_FOUND);
        }
        if (prodotto.getStatorichiesta() != StatoContenuto.ACCETTATA) {
            return new ResponseEntity<>("Il prodotto deve essere stato precedentemente validato'", HttpStatus.BAD_REQUEST);
        }
        if (pacchettoService.existsPacchetto(idPacchetto)) {
            if (pacchettoService.getPacchetto(idPacchetto).getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            if (pacchettoService.aggiungiProdotto(idPacchetto, idProdotto)) {
                return new ResponseEntity<>("Prodotto aggiunto al pacchetto", HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Prodotto già esistente nel pacchetto", HttpStatus.CONFLICT);
        } return new ResponseEntity<>("Pacchetto non esiste", HttpStatus.CONFLICT);
    }

    @PostMapping({"/pacchetti/aggiungiconparametri"})
    public ResponseEntity<Object> aggiungiPacchettoConParametri(@RequestParam("nome") String nome,
                                                                @RequestParam("descrizione") String descrizione,
                                                                @RequestParam("prezzo") double prezzo,
                                                                @RequestParam("prodottiSet") Set<Integer> idProdottiSet) {
        if (idProdottiSet.size() < 2) {
            return new ResponseEntity<>("Il pacchetto deve contenere almeno 2 prodotti", HttpStatus.BAD_REQUEST);
        }
        for (Integer idProdotto : idProdottiSet) {
            Prodotto prodotto = prodottoService.getProdottoById(idProdotto);
            if (prodotto == null) {
                return new ResponseEntity<>("Prodotto con ID " + idProdotto + " non trovato", HttpStatus.NOT_FOUND);
            }

            if (prodotto.getStatorichiesta() != StatoContenuto.ACCETTATA) {
                return new ResponseEntity<>("Il prodotto con ID " + idProdotto + " deve essere stato precedentemente validato", HttpStatus.BAD_REQUEST);
            }
        }

        if (pacchettoService.aggiungiPacchettoConParametri(nome, descrizione, prezzo, idProdottiSet)) {
            return new ResponseEntity<>("Pacchetto aggiunto", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Pacchetto già esistente", HttpStatus.CONFLICT);
    }


    @RequestMapping({"/pacchetti/elimina/{id}"})
    public ResponseEntity<Object> eliminaPacchetto(@PathVariable("id") int id) {
        if (pacchettoService.existsPacchetto(id)) {
            if (pacchettoService.getPacchetto(id).getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            pacchettoService.eliminaPacchetto(id);
            return new ResponseEntity<>("Pacchetto eliminato", HttpStatus.OK);
        } else return new ResponseEntity<>("Pacchetto non trovato", HttpStatus.NOT_FOUND);
    }

    @RequestMapping({"/pacchetti/elimina/prodotto"})
    public ResponseEntity<Object> eliminaProdotto(@RequestParam("idPacchetto") int id,
                                                  @RequestParam("idProdotto") int idProdotto) {
        if (pacchettoService.existsPacchetto(id)) {
            if (prodottoService.existsProdotto(idProdotto)) {
                if (pacchettoService.getPacchetto(id).getOperatore() != userService.getCurrentUser()) {
                    return unauthorizedResponse();
                }
                if (pacchettoService.eliminaProdotto(id, idProdotto)) {
                    if (pacchettoService.getPacchetto(id).getProdottiSet().size() < 2) {
                        pacchettoService.eliminaPacchetto(id);
                        return new ResponseEntity<>("Prodotto eliminato e pacchetto rimosso perché contiene meno di 2 prodotti", HttpStatus.OK);
                    }
                    return new ResponseEntity<>("Prodotto eliminato", HttpStatus.OK);
                }
                return new ResponseEntity<>("Prodotto non eliminato", HttpStatus.CONFLICT);
            } else return new ResponseEntity<>("Prodotto non trovato", HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<>("Pacchetto non trovato", HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/pacchetti/aggiorna")
    public ResponseEntity<Object> aggiornaPacchetto(@RequestBody PacchettoAggiornatoDTO pacchettoAggiornatoDTO) {
        if (pacchettoService.existsPacchetto(pacchettoAggiornatoDTO.getId())) {
            Pacchetto pacchetto = pacchettoService.getPacchetto(pacchettoAggiornatoDTO.getId());
            if (pacchetto.getOperatore() != userService.getCurrentUser()) {
                return unauthorizedResponse();
            }
            pacchetto.setNome(pacchettoAggiornatoDTO.getNome());
            pacchetto.setDescrizione(pacchettoAggiornatoDTO.getDescrizione());
            pacchetto.setPrezzo(pacchettoAggiornatoDTO.getPrezzo());
            Set<Prodotto> prodotti = new HashSet<>();
            for (Integer idProdotto : pacchettoAggiornatoDTO.getProdottiIds()) {
                Prodotto prodotto = prodottoService.getProdottoById(idProdotto);
                prodotti.add(prodotto);
            }
            pacchetto.setProdottiSet(prodotti);

            if (pacchettoService.aggiornaPacchetto(pacchetto)) {
                return new ResponseEntity<>("Pacchetto aggiornato", HttpStatus.OK);
            }
            return new ResponseEntity<>("Pacchetto non aggiornato", HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>("Pacchetto non trovato", HttpStatus.NOT_FOUND);
        }
    }
}