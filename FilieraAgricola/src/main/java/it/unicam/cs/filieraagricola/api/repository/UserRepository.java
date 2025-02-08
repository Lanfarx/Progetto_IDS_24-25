package it.unicam.cs.filieraagricola.api.repository;

import it.unicam.cs.filieraagricola.api.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) = 1 THEN TRUE ELSE FALSE END " +
            "FROM Users u WHERE u.username = :username")
    boolean existsByUsername(String username);
}