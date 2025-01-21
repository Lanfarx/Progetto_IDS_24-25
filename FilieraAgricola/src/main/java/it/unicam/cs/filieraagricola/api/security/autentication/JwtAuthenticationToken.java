package it.unicam.cs.filieraagricola.api.security.autentication;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final UserDetails userDetails;

    public JwtAuthenticationToken(UserDetails userDetails, String token) {
        super(userDetails.getAuthorities());
        this.userDetails = userDetails;
        this.token = token;
        setAuthenticated(true); // Impostato su true perché l'utente è autenticato con il JWT
    }

    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.userDetails = null;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token; // La credenziale è il token JWT
    }

    @Override
    public Object getPrincipal() {
        return userDetails; // Il principal è l'utente che ha il token
    }

    public String getToken() {
        return token;
    }
}