package it.unicam.cs.filieraagricola.api.services.elemento;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.Categoria;
import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProdottoTrasformatoService extends ProdottoService<ProdottoTrasformato> {
    @Autowired
    private UserService userService;

    public ProdottoTrasformato getProdottoTrasformato(int id) {
        if (this.prodottoRepository.existsProdottoTrasformatoById(id)) {
            return prodottoRepository.findProdottoTrasformatoById(id).get();
        } else {
            return null;
        }
    }

    public List<ProdottoTrasformato> getProdottiTrasformati() {
        return prodottoRepository.findProdottiTrasformatiByOperatore(userService.getCurrentUser());
    }

    public List<ProdottoTrasformato> getAllProdottiTrasformatiValidi() {
        return prodottoRepository.findProdottiTrasformatoByStatorichiestaEquals(StatoContenuto.ACCETTATA);
    }

    public void deleteProdottoTrasformato(int id) {
        Set<Pacchetto> pacchettoSet = pacchettoService.getPacchettiConProdotto(id);
        this.prodottoRepository.deleteById(id);
        pacchettoService.checkAndDelete(pacchettoSet);
    }

    public boolean aggiornaProdottoTrasformato(ProdottoTrasformato prodottoTrasformato) {
        if (this.prodottoRepository.existsProdottoTrasformatoById(prodottoTrasformato.getId())) {
            prodottoTrasformato.setOperatore(userService.getCurrentUser());
            prodottoTrasformato.setCategoria(getProdottoTrasformato(prodottoTrasformato.getId()).getCategoria());
            this.prodottoRepository.save(prodottoTrasformato);
            return true;
        } else {
            return false;
        }
    }

    public boolean aggiungiProdottoTrasformato(String nome, String processo,
                                               String certificazioni, int prodottoBaseID,
                                               String descrizione, double prezzo, Categoria categoria) {
        Optional<ProdottoBase> prodottoBaseOpt = prodottoRepository.findProdottoBaseById(prodottoBaseID);
        ProdottoBase prodottoBase;
        if(prodottoBaseOpt.isPresent()) {
            prodottoBase = prodottoBaseOpt.get();
        } else {
            return false;
        }
        if(prodottoRepository.existsByCaratteristicheTrasformato(nome, processo, certificazioni, prodottoBase)){
            return false;
        }
        creaTrasformato(nome, processo, certificazioni, prodottoBaseID, descrizione, prezzo, categoria);
        return true;
    }

    public boolean aggiungiProdottoTrasformato(ProdottoTrasformato prodottoTrasformato) {
        if(prodottoRepository.existsByCaratteristicheBase(prodottoTrasformato.getNome(), prodottoTrasformato.getProcessoTrasformazione(),prodottoTrasformato.getCertificazioni())) {
            return false;
        }
        Categoria categoria = categoriaService.getCategoriaByNome(prodottoTrasformato.getCategoria().getNome()).get();
        prodottoTrasformato.setCategoria(categoria);
        prodottoTrasformato.setOperatore(userService.getCurrentUser());
        prodottoRepository.save(prodottoTrasformato);
        return true;
    }


    public void creaTrasformato(String nome, String processo, String certificazioni, int prodottoBaseID,
                                String descrizione, double prezzo, Categoria categoria) {
        ProdottoTrasformato prodottoTrasformato = new ProdottoTrasformato();
        ProdottoBase prodottoBase = prodottoRepository.findProdottoBaseById(prodottoBaseID).get(); //Il controllo per esistenza viene già effettuato in esisteProdottoTrasformato
        prodottoTrasformato.setNome(nome);
        prodottoTrasformato.setProcessoTrasformazione(processo);
        prodottoTrasformato.setCertificazioni(certificazioni);
        prodottoTrasformato.setProdottoBase(prodottoBase);
        prodottoTrasformato.setDescrizione(descrizione);
        prodottoTrasformato.setPrezzo(prezzo);
        prodottoTrasformato.setOperatore(userService.getCurrentUser());
        prodottoTrasformato.setCategoria(categoria);
        prodottoRepository.save(prodottoTrasformato);
    }

    public boolean aggiornaQuantitaProdotto(int id, int quantita) {
        ProdottoTrasformato prodotto = prodottoRepository.findProdottoTrasformatoById(id).orElse(null);
        if (prodotto != null) {
            prodotto.setQuantita(prodotto.getQuantita() + quantita);
            prodottoRepository.save(prodotto);
            return true;
        }
        return false;
    }

    public boolean riduciQuantitaProdotto(int id, int quantita) {
        ProdottoTrasformato prodotto = prodottoRepository.findProdottoTrasformatoById(id).orElse(null);
        if (prodotto != null) {
            if (prodotto.getQuantita() >= quantita) {
                prodotto.setQuantita(prodotto.getQuantita() - quantita);
                prodottoRepository.save(prodotto);
            } else {
                deleteProdottoTrasformato(id);
            }
            return true;
        }
        return false;
    }

    public boolean existsProdottoTrasformato(int id) {
        return prodottoRepository.existsProdottoTrasformatoById(id);
    }
}