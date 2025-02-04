package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.repository.RichiestaRepository;
import it.unicam.cs.filieraagricola.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private RichiestaRepository richiestaRepository;

    public UserService(UserRepository userRepository, RichiestaRepository richiestaRepository) {
        this.userRepository = userRepository;
        this.richiestaRepository = richiestaRepository;
    }

    public Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsUser(int id) {
        return userRepository.existsById(id);
    }

    public Optional<Users> getUserById(int id) {
        return userRepository.findById(id);
    }

    public void save(Users user) {
        userRepository.save(user);
    }

    public void delete(Users user) {
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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


    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).get();
    }


    public boolean isOperatore(Users user) {
        return user.getRoles().contains(UserRole.PRODUTTORE) ||
                user.getRoles().contains(UserRole.TRASFORMATORE) ||
                user.getRoles().contains(UserRole.DISTRIBUTORE_DI_TIPICITA);
    }

    public Set<Users> getOperatoriByIds(Set<Integer> idInvitati) {
        Set<Users> operatori = new HashSet<>();
        for (Integer id : idInvitati) {
            Optional<Users> user = userRepository.findById(id);
            if (user.isPresent() && isOperatore(user.get())) {
                operatori.add(user.get());
            }
        }
        return operatori;
    }

    public void processaInvitati(Set<Integer> idInvitati, Set<Users> operatori, Set<Integer> nonOperatori) {
        for (Integer id : idInvitati) {
            Optional<Users> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                if (isOperatore(user)) {
                    operatori.add(user);
                } else {
                    nonOperatori.add(id);
                }
            } else {
                nonOperatori.add(id);
            }
        }
    }
}

