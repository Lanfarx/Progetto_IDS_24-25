
package it.unicam.cs.filieraagricola.api.Examples;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductListRepository extends JpaRepository<Product, String> {
    }
