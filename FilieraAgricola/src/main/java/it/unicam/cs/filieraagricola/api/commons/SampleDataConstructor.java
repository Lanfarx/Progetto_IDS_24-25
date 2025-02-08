package it.unicam.cs.filieraagricola.api.commons;

import it.unicam.cs.filieraagricola.api.entities.*;
import it.unicam.cs.filieraagricola.api.entities.attivita.Evento;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.entities.elemento.Pacchetto;
import it.unicam.cs.filieraagricola.api.entities.elemento.Prodotto;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoTrasformato;
import it.unicam.cs.filieraagricola.api.repository.*;
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
    private AttivitaRepository attivitaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    ProdottoRepository prodottoRepository;
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

        Users curatore = new Users();
        curatore.setUsername("curatore");
        curatore.setPassword("curatore");
        curatore.getRoles().add(UserRole.CURATORE);
        userRepository.save(curatore);
    }

    private void initSampleAttivita(){
        Users organizzatore1 = new Users();
        organizzatore1.setUsername("organizzatore1");
        organizzatore1.setPassword("organizzatore1");
        organizzatore1.getRoles().add(UserRole.ANIMATORE_DELLA_FILIERA);

        Users organizzatore2 = new Users();
        organizzatore2.setUsername("organizzatore2");
        organizzatore2.setPassword("organizzatore2");
        organizzatore2.getRoles().add(UserRole.ANIMATORE_DELLA_FILIERA);

        userRepository.save(organizzatore1);
        userRepository.save(organizzatore2);

        Visita visita = new Visita();
        visita.setTitolo("ProvaVisita");
        visita.setOrganizzatore(organizzatore1);
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
        evento.setOrganizzatore(organizzatore2);
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
        Users operatore1 = new Users();
        operatore1.setUsername("operatore1");
        operatore1.setPassword("operatore1");
        operatore1.getRoles().add(UserRole.PRODUTTORE);

        Users operatore2 = new Users();
        operatore2.setUsername("operatore2");
        operatore2.setPassword("operatore2");
        operatore2.getRoles().add(UserRole.TRASFORMATORE);

        userRepository.save(operatore1);
        userRepository.save(operatore2);

        ProdottoBase mela = new ProdottoBase();
        mela.setNome("Mela");
        mela.setCertificazioni("Certificazione");
        mela.setDescrizione("Prodotto di prova");
        mela.setMetodiDiColtivazione("Coltivato in arabia");
        mela.setPrezzo(50);
        mela.setQuantita(50);
        mela.setOperatore(operatore1);
        prodottoRepository.save(mela);

        Set<Prodotto> prodotti = new HashSet<>();
        ProdottoBase prodottoBase = new ProdottoBase();
        prodottoBase.setNome("Pomodoro");
        prodottoBase.setCertificazioni("Certificazione");
        prodottoBase.setDescrizione("Prodotto di prova");
        prodottoBase.setMetodiDiColtivazione("Coltivato in italia");
        prodottoBase.setPrezzo(50);
        prodottoBase.setQuantita(50);
        prodottoBase.setOperatore(operatore1);
        prodottoRepository.save(prodottoBase);
        prodotti.add(prodottoBase);

        ProdottoTrasformato prodottoTrasformato = new ProdottoTrasformato();
        prodottoTrasformato.setNome("Passata di pomodoro");
        prodottoTrasformato.setCertificazioni("Certificazione");
        prodottoTrasformato.setDescrizione("Prodotto di passata");
        prodottoTrasformato.setProdottoBase(prodottoBase);
        prodottoTrasformato.setProcessoTrasformazione("Sch9iaccciaot");
        prodottoTrasformato.setPrezzo(50);
        prodottoTrasformato.setOperatore(operatore2);
        prodottoRepository.save(prodottoTrasformato);
        prodotti.add(prodottoTrasformato);
        initSamplePacchetto(prodotti);
    }

    private void initSamplePacchetto(Set<Prodotto> prodottoSet){
        Users operatore3 = new Users();
        operatore3.setUsername("operatore3");
        operatore3.setPassword("operatore3");
        operatore3.getRoles().add(UserRole.DISTRIBUTORE_DI_TIPICITA);

        userRepository.save(operatore3);

        Pacchetto pacchetto = new Pacchetto();
        pacchetto.setNome("Pacchetto");
        pacchetto.setDescrizione("Pacchetto di prova");
        pacchetto.setPrezzo(40);
        pacchetto.setProdottiSet(prodottoSet);
        pacchetto.setOperatore(operatore3);
        pacchettoRepository.save(pacchetto);
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
