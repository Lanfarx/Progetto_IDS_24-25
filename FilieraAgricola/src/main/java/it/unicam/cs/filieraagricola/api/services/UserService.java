package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Recuperiamo l'utente dal database
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Se i ruoli sono memorizzati come Set<UserRole>, li convertiamo in SimpleGrantedAuthority
        Collection<? extends GrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())) // Converte UserRole in SimpleGrantedAuthority
                        .collect(Collectors.toList()); // Raccoglie i ruoli in una lista di GrantedAuthority

        return new User(user.getUsername(),
                user.getPassword(),
                authorities);
    }

    public void registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username già esistente");
        }
        Users newUser = new Users();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.getRoles().add(UserRole.ACQUIRENTE);
        userRepository.save(newUser);
    }
}

