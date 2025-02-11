package it.unicam.cs.filieraagricola.api.commons;

import it.unicam.cs.filieraagricola.api.commons.richiesta.StatoContenuto;
import it.unicam.cs.filieraagricola.api.entities.*;
import it.unicam.cs.filieraagricola.api.entities.attivita.Evento;
import it.unicam.cs.filieraagricola.api.entities.attivita.Visita;
import it.unicam.cs.filieraagricola.api.entities.elemento.*;
import it.unicam.cs.filieraagricola.api.repository.*;
import it.unicam.cs.filieraagricola.api.services.gestore.CategoriaService;
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
    private ProdottoRepository prodottoRepository;
    @Autowired
    private PacchettoRepository pacchettoRepository;
    @Autowired
    private CategoriaService categoriaService;



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

    private void initSampleCategorie() {
        List<String> categorie = List.of("Vini", "Formaggi",
                "Salumi", "Olio d'oliva", "Miele", "Pacchetto", "Frutta");

        for (String nome : categorie) {
            if (categoriaRepository.findByNome(nome).isEmpty()) {
                Categoria categoria = new Categoria();
                categoria.setNome(nome);
                categoriaRepository.save(categoria);
            }
        }
    }

    private void initSampleProdotti(){
        Users produttore = new Users();
        produttore.setUsername("produttore");
        produttore.setPassword("produttore");
        produttore.getRoles().add(UserRole.PRODUTTORE);

        Users trasformatore = new Users();
        trasformatore.setUsername("trasformatore");
        trasformatore.setPassword("trasformatore");
        trasformatore.getRoles().add(UserRole.TRASFORMATORE);

        userRepository.save(produttore);
        userRepository.save(trasformatore);

        ProdottoBase mela = new ProdottoBase();
        mela.setNome("Mela");
        mela.setCertificazioni("Certificazione Bio");
        mela.setDescrizione("Mele fresche e croccanti");
        mela.setMetodiDiColtivazione("Coltivate in montagna");
        mela.setPrezzo(30);
        mela.setQuantita(100);
        mela.setOperatore(produttore);
        mela.setStatorichiesta(StatoContenuto.ACCETTATA);
        mela.setCategoria(categoriaService.getCategoriaByNome("Frutta").get());
        prodottoRepository.save(mela);

        ProdottoBase pomodoro = new ProdottoBase();
        pomodoro.setNome("Pomodoro");
        pomodoro.setCertificazioni("Certificazione DOC");
        pomodoro.setDescrizione("Pomodori succosi e maturi");
        pomodoro.setMetodiDiColtivazione("Serra biologica");
        pomodoro.setPrezzo(50);
        pomodoro.setQuantita(80);
        pomodoro.setOperatore(produttore);
        pomodoro.setStatorichiesta(StatoContenuto.ACCETTATA);
        pomodoro.setCategoria(categoriaService.getCategoriaByNome("Frutta").get());
        prodottoRepository.save(pomodoro);

        ProdottoBase latte = new ProdottoBase();
        latte.setNome("Latte fresco");
        latte.setCertificazioni("DOP");
        latte.setDescrizione("Latte di alta qualità da mucche da pascolo");
        latte.setMetodiDiColtivazione("Allevamento sostenibile");
        latte.setPrezzo(20);
        latte.setQuantita(50);
        latte.setOperatore(produttore);
        latte.setStatorichiesta(StatoContenuto.ACCETTATA);
        latte.setCategoria(categoriaService.getCategoriaByNome("Formaggi").get());
        prodottoRepository.save(latte);

        Set<Prodotto> pacchetto1 = new HashSet<>();
        pacchetto1.add(mela);
        pacchetto1.add(pomodoro);
        pacchetto1.add(latte);

        ProdottoTrasformato passataPomodoro = new ProdottoTrasformato();
        passataPomodoro.setNome("Passata di pomodoro");
        passataPomodoro.setCertificazioni("BIO");
        passataPomodoro.setDescrizione("Passata di pomodoro artigianale");
        passataPomodoro.setProdottoBase(pomodoro);
        passataPomodoro.setProcessoTrasformazione("Pomodori schiacciati e cotti lentamente");
        passataPomodoro.setCategoria(categoriaService.getCategoriaByNome("Frutta").get());
        passataPomodoro.setPrezzo(70);
        passataPomodoro.setQuantita(20);
        passataPomodoro.setOperatore(trasformatore);
        passataPomodoro.setStatorichiesta(StatoContenuto.ACCETTATA);
        prodottoRepository.save(passataPomodoro);
        pacchetto1.add(passataPomodoro);

        ProdottoTrasformato formaggioStagionato = new ProdottoTrasformato();
        formaggioStagionato.setNome("Formaggio stagionato");
        formaggioStagionato.setCertificazioni("DOP");
        formaggioStagionato.setDescrizione("Formaggio stagionato 12 mesi");
        formaggioStagionato.setProdottoBase(latte);
        formaggioStagionato.setProcessoTrasformazione("Latte fermentato e lasciato stagionare");
        formaggioStagionato.setCategoria(categoriaService.getCategoriaByNome("Formaggi").get());
        formaggioStagionato.setPrezzo(90);
        formaggioStagionato.setQuantita(15);
        formaggioStagionato.setOperatore(trasformatore);
        formaggioStagionato.setStatorichiesta(StatoContenuto.ACCETTATA);
        prodottoRepository.save(formaggioStagionato);
        pacchetto1.add(formaggioStagionato);

        ProdottoBase pera = new ProdottoBase();
        pera.setNome("Pera");
        pera.setCertificazioni("Certificazione");
        pera.setDescrizione("Prodotto di prova");
        pera.setMetodiDiColtivazione("Coltivato in spagna");
        pera.setPrezzo(45);
        pera.setQuantita(30);
        pera.setOperatore(produttore);
        pera.setCategoria(categoriaService.getCategoriaByNome("Frutta").get());
        pera.setStatorichiesta(StatoContenuto.ACCETTATA);
        prodottoRepository.save(pera);

        Set<Prodotto> pacchetto2 = new HashSet<>();
        pacchetto2.add(pera);

        ProdottoBase arancia = new ProdottoBase();
        arancia.setNome("Arancia");
        arancia.setCertificazioni("Certificazione");
        arancia.setDescrizione("Prodotto di prova");
        arancia.setMetodiDiColtivazione("Coltivato in sicilia");
        arancia.setCategoria(categoriaService.getCategoriaByNome("Frutta").get());
        arancia.setPrezzo(60);
        arancia.setQuantita(40);
        arancia.setOperatore(produttore);
        arancia.setStatorichiesta(StatoContenuto.ACCETTATA);
        prodottoRepository.save(arancia);
        pacchetto2.add(arancia);

        ProdottoTrasformato marmellataArancia = new ProdottoTrasformato();
        marmellataArancia.setNome("Marmellata di arancia");
        marmellataArancia.setCertificazioni("Certificazione");
        marmellataArancia.setDescrizione("Prodotto di marmellata");
        marmellataArancia.setProdottoBase(arancia);
        marmellataArancia.setProcessoTrasformazione("Marmellizzazione");
        marmellataArancia.setCategoria(categoriaService.getCategoriaByNome("Frutta").get());
        marmellataArancia.setPrezzo(75);
        marmellataArancia.setQuantita(5);
        marmellataArancia.setOperatore(trasformatore);
        marmellataArancia.setStatorichiesta(StatoContenuto.ACCETTATA);
        prodottoRepository.save(marmellataArancia);
        pacchetto2.add(marmellataArancia);

        initSamplePacchetto(pacchetto1, pacchetto2);

        ProdottoBase banana = new ProdottoBase();
        banana.setNome("Banana");
        banana.setCertificazioni("Fair Trade");
        banana.setDescrizione("Banane dolci e nutrienti, ideali per uno spuntino sano");
        banana.setMetodiDiColtivazione("Coltivate come in Sud America con pratiche sostenibili");
        banana.setCategoria(categoriaService.getCategoriaByNome("Frutta").get());
        banana.setPrezzo(45);
        banana.setQuantita(50);
        banana.setOperatore(produttore);
        banana.setStatorichiesta(StatoContenuto.RIFIUTATA);
        prodottoRepository.save(banana);
    }

    private void initSamplePacchetto(Set<Prodotto> pacchetto1prodotti, Set<Prodotto> pacchetto2prodotti) {
        Users distributore = new Users();
        distributore.setUsername("distributore");
        distributore.setPassword("distributore");
        distributore.getRoles().add(UserRole.DISTRIBUTORE_DI_TIPICITA);
        userRepository.save(distributore);

        Pacchetto pacchetto1 = new Pacchetto();
        pacchetto1.setNome("Pacchetto Degustazione");
        pacchetto1.setDescrizione("Un mix di prodotti freschi e trasformati");
        pacchetto1.setPrezzo(120);
        pacchetto1.setProdottiSet(pacchetto1prodotti);
        pacchetto1.setOperatore(distributore);
        pacchetto1.setStatorichiesta(StatoContenuto.ACCETTATA);
        pacchetto1.setCategoria(categoriaService.getCategoriaByNome("Pacchetto").get());
        pacchettoRepository.save(pacchetto1);

        Pacchetto pacchetto2 = new Pacchetto();
        pacchetto2.setNome("Pacchetto Merenda");
        pacchetto2.setDescrizione("Un'ottima combinazione di sapori");
        pacchetto2.setPrezzo(100);
        pacchetto2.setProdottiSet(pacchetto2prodotti);
        pacchetto2.setOperatore(distributore);
        pacchetto2.setCategoria(categoriaService.getCategoriaByNome("Pacchetto").get());
        pacchettoRepository.save(pacchetto2);
    }
}
