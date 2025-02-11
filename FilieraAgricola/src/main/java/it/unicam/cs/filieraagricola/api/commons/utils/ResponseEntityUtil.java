package it.unicam.cs.filieraagricola.api.commons.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {

    public static ResponseEntity<Object> unauthorizedResponse() {
        return new ResponseEntity<>("Non hai i permessi per accedere o modificare questo contenuto", HttpStatus.FORBIDDEN);
    }
}