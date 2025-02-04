package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.entities.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.ProdottoBase;
import it.unicam.cs.filieraagricola.api.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdottoBaseService {
    @Autowired
    private final ProdottoRepository prodottoRepository;
    @Autowired
    private PacchettoService pacchettoService;
    @Autowired
    private ContenutoService contenutoService;

    public ProdottoBaseService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }


    public boolean aggiungiProdottoBase(String nome, String metodiColtivazione,
                                                       String certificazioni, String descrizione, double prezzo) {
        if(prodottoRepository.existsByCaratteristicheBase(nome, metodiColtivazione, certificazioni)){
            return false;
        }
        creaBase(nome, metodiColtivazione, certificazioni, descrizione, prezzo);
        return true;
    }

    public boolean aggiungiProdottoBase(ProdottoBase prodottoBase) {
        if(prodottoRepository.existsById(prodottoBase.getId()) || contenutoService.getContenutoByParam(prodottoBase) != null) {
            return false;
        }
        prodottoRepository.save(prodottoBase);
        contenutoService.aggiungiContenutoDaElemento(prodottoBase);
        return true;
    }

    public ProdottoBase getProdottoBase(int id) {
        if (this.prodottoRepository.existsProdottoBaseById(id)) {
            return prodottoRepository.findProdottoBaseById(id).get();
        } else {
            return null;
        }
    }

    public List<ProdottoBase> getProdottiBase() {
        return this.prodottoRepository.findAllProdottiBase();
    }

    public void deleteProdottoBase(int id) {
        /*if(!pacchettoService.getPacchettiConProdotto(id).isEmpty()) {
            for(Pacchetto pacchetto : pacchettoService.getPacchettiConProdotto(id)) {
                pacchettoService.eliminaProdotto(pacchetto.getId(), id);
            }
        }*/
        this.prodottoRepository.deleteProdottiTrasformatiByProdottoBaseId(id);
        this.prodottoRepository.deleteProdottoBaseById(id);
    }

    public boolean aggiornaProdottoBase(ProdottoBase prodottoBase) {
        if (this.prodottoRepository.existsProdottoBaseById(prodottoBase.getId())) {
            this.prodottoRepository.save(prodottoBase);
            return true;
        } else {
            return false;
        }
    }

    public void creaBase(String nome, String metodiColtivazione, String certificazioni, String descrizone, double prezzo) {
        ProdottoBase prodottoBase = new ProdottoBase();
        prodottoBase.setNome(nome);
        prodottoBase.setMetodiDiColtivazione(metodiColtivazione);
        prodottoBase.setCertificazioni(certificazioni);
        prodottoBase.setDescrizione(descrizone);
        prodottoBase.setPrezzo(prezzo);
        contenutoService.aggiungiContenutoDaElemento(prodottoBase);
        prodottoRepository.save(prodottoBase);
    }
}