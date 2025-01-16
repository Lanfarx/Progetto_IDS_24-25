package it.unicam.cs.filieraagricola.api.RestWebService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductExceptionController {
    public ProductExceptionController() {
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<Object> exception(ProductNotFoundException exception) {
        return new ResponseEntity<>("Prodotto non trovato nel database", HttpStatus.NOT_FOUND);
    }
}
