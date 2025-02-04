package it.unicam.cs.filieraagricola.api.commons;

import it.unicam.cs.filieraagricola.api.entities.*;
import it.unicam.cs.filieraagricola.api.entities.attivita.Evento;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.repository.*;
import it.unicam.cs.filieraagricola.api.services.ContenutoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SampleDataConstructor {

    @Autowired
    AttivitaRepository attivitaRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    ProdottoRepository prodottoRepository;
    @Autowired
    ContenutoService contenutoService;
    @Autowired
    PacchettoRepository pacchettoRepository;

    @PostConstruct
    public void initSampleData(){
        initSampleUsers();
        initSampleAttivita();
        initSampleCategorie();
        initSampleProdotti(); //Chiama anche il metodo per il pacchetto
    }

    private void initSampleUsers(){
        Users admin = new Users();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.getRoles().add(UserRole.GESTORE_DELLA_PIATTAFORMA);
        userRepository.save(admin);

        Users operatore = new Users();
        operatore.setUsername("operatore");
        operatore.setPassword("operatore");
        operatore.getRoles().add(UserRole.PRODUTTORE);
        operatore.getRoles().add(UserRole.TRASFORMATORE);
        operatore.getRoles().add(UserRole.DISTRIBUTORE_DI_TIPICITA);
        userRepository.save(operatore);

        Users animatore = new Users();
        animatore.setUsername("animatore");
        animatore.setPassword("animatore");
        animatore.getRoles().add(UserRole.ANIMATORE_DELLA_FILIERA);
        userRepository.save(animatore);
    }

    private void initSampleAttivita(){
        Visita visita = new Visita();
        visita.setTitolo("ProvaVisita");
        visita.setDescrizione("questa è una visita");
        visita.setLuogo("Genzano");
        visita.setData(LocalDate.parse("2025-01-27"));

        Users user1 = new Users();
        user1.setUsername("acquirente");
        user1.setPassword("acquirente");
        user1.getRoles().add(UserRole.ACQUIRENTE);
        userRepository.save(user1);
        visita.getPrenotazioni().add(user1);

        attivitaRepository.save(visita);

        Evento evento = new Evento();
        evento.setTitolo("Baudo");
        evento.setDescrizione("questo è un evento");
        evento.setLuogo("Civitanova");
        evento.setData(LocalDate.parse("2025-06-26"));;

        Users user2 = new Users();
        user2.setUsername("invitato");
        user2.setPassword("invitato");
        user1.getRoles().add(UserRole.PRODUTTORE);
        userRepository.save(user2);

        evento.getPrenotazioni().add(user1);
        evento.getInvitati().add(user2);

        attivitaRepository.save(evento);
    }

    private void initSampleProdotti(){
        Set<Prodotto> prodotti = new HashSet<>(); //Creo un set per darlo al pacchetto
        ProdottoBase prodottoBase = new ProdottoBase();
        prodottoBase.setNome("Pomodoro");
        prodottoBase.setCertificazioni("Certificazione");
        prodottoBase.setDescrizione("Prodotto di prova");
        prodottoBase.setMetodiDiColtivazione("Coltivato in italia");
        prodottoBase.setPrezzo(50);
        prodottoBase.setQuantita(50);
        prodottoRepository.save(prodottoBase);
        contenutoService.aggiungiContenutoDaElemento(prodottoBase);
        prodotti.add(prodottoBase);
        ProdottoTrasformato prodottoTrasformato = new ProdottoTrasformato();
        prodottoTrasformato.setNome("Passata di pomodoro");
        prodottoTrasformato.setCertificazioni("Certificazione");
        prodottoTrasformato.setDescrizione("Prodotto di passata");
        prodottoTrasformato.setProdottoBase(prodottoBase);
        prodottoTrasformato.setProcessoTrasformazione("Sch9iaccciaot");
        prodottoTrasformato.setPrezzo(50);
        prodottoRepository.save(prodottoTrasformato);
        contenutoService.aggiungiContenutoDaElemento(prodottoTrasformato);
        prodotti.add(prodottoTrasformato);
        initSamplePacchetto(prodotti); //chiamata per creare un pacchetto
    }

    private void initSamplePacchetto(Set<Prodotto> prodottoSet){
        Pacchetto pacchetto = new Pacchetto();
        pacchetto.setNome("Pacchetto");
        pacchetto.setDescrizione("Pacchetto di prova");
        pacchetto.setPrezzo(40);
        pacchetto.setProdottiSet(prodottoSet);
        pacchettoRepository.save(pacchetto);
        contenutoService.aggiungiContenutoDaElemento(pacchetto);
    }
    private void initSampleCategorie() {
        List<String> categorie = List.of("Vini", "Formaggi",
                "Salumi", "Olio d'oliva", "Miele", "Pacchetto");

        for (String nome : categorie) {
            if (categoriaRepository.findByNome(nome).isEmpty()) {
                Categoria categoria = new Categoria();
                categoria.setNome(nome);
                categoriaRepository.save(categoria);
            }
        }
    }
}
