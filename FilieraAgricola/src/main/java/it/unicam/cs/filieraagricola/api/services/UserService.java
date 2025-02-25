package it.unicam.cs.filieraagricola.api.services;

import it.unicam.cs.filieraagricola.api.commons.UserRole;
import it.unicam.cs.filieraagricola.api.entities.Users;
import it.unicam.cs.filieraagricola.api.entities.elemento.ProdottoBase;
import it.unicam.cs.filieraagricola.api.repository.UserRepository;
import it.unicam.cs.filieraagricola.api.services.elemento.ProdottoBaseService;
import jakarta.transaction.Transactional;
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
    private  UserRepository userRepository;
    @Autowired
    private ProdottoBaseService prodottoBaseService;

    public Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
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

    @Transactional
    public void delete(Users user) {
        deleteProdottiBase(user);
        userRepository.flush();
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Collection<? extends GrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList());

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

    public void aggiungiRuolo(Integer userId, UserRole role) {
        if(userRepository.existsById(userId)) {
            Users user = userRepository.findById(userId).get();
            if(!user.getRoles().contains(role)) {
                user.getRoles().add(role );
                userRepository.save(user);
            } else throw new RuntimeException("Utente già possessore del ruolo");
        } else throw new RuntimeException("Utente non esiste");
    }

    public void rimuoviRuolo(Integer userId, UserRole role) {
        if(userRepository.existsById(userId)) {
            Users user = userRepository.findById(userId).get();
            if(user.getRoles().contains(role)) {
                user.getRoles().remove(role);
                userRepository.save(user);
            }  else throw new RuntimeException("Utente non possessore del ruolo");
        } else throw new RuntimeException("Utente non esiste");
    }

    public boolean verifyPassword(String password , String userPassword) {
        return password.equals(userPassword);
    }

    public void modificaCredenziali(Users user, String username, String password) {
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
    }

    protected void deleteProdottiBase(Users user) {
        for (ProdottoBase prodottoBase : prodottoBaseService.getAllProdottiBaseByUser(user)) {
            this.prodottoBaseService.deleteProdottoBase(prodottoBase.getId());
        }
    }
}

