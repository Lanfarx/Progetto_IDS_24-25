package it.unicam.cs.filieraagricola.api.security.autentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private String secretKey = "secret"; // Usa una chiave segreta più sicura in produzione
    private long expirationTime = 86400000; // 24 ore in millisecondi

    // Metodo per generare il token JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Metodo per estrarre il nome utente dal token JWT
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Metodo per verificare se il token è scaduto
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Metodo per ottenere la data di scadenza del token
    private Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // Metodo per validare il token
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
