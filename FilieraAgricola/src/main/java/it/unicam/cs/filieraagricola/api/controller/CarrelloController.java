package it.unicam.cs.filieraagricola.api.controller;

import it.unicam.cs.filieraagricola.api.entities.carrello.Carrello;
import it.unicam.cs.filieraagricola.api.services.carrello.CarrelloService;
import it.unicam.cs.filieraagricola.api.services.carrello.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autenticato/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private OrdineService ordineService;

    @PostMapping("/aggiungi}")
    public ResponseEntity<String> aggiungiElementoAlCarrello(@RequestParam int userId, @RequestParam int id, @RequestParam int quantita) {
        carrelloService.aggiungiAlCarrello(userId, itemId, quantity);
        return ResponseEntity.ok("Item added to cart");
    }

    @PostMapping("/rimuovi")
    public ResponseEntity<String> rimuoviElementoDalCarrello(@RequestParam Long userId, @PathVariable Long itemId) {
        carrelloService.removeItemFromCart(userId, itemId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @GetMapping("/view")
    public ResponseEntity<Carrello> viewCart(@RequestParam Long userId) {
        Carrello cart = carrelloService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@RequestParam Long userId) {
        Order order = orderManagerService.createOrderFromCart(userId);
        return ResponseEntity.ok(order);
    }
}
