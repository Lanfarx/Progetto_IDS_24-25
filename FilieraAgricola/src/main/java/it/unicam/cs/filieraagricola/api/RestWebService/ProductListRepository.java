
package it.unicam.cs.filieraagricola.api.RestWebService;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductListRepository extends JpaRepository<Product, String> {
    }
