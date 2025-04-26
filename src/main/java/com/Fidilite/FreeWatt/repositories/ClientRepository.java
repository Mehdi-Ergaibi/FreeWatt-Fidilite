package com.Fidilite.FreeWatt.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Fidilite.FreeWatt.Entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNom(String nom);
    @Query("SELECT c FROM Client c WHERE c.derniereDateAchat < :dateLimite")
    List<Client> findClientsInactifs(@Param("dateLimite") LocalDateTime dateLimite);
    List<Client> findAllByOrderByTotalDepensesDesc();
    Page<Client> findByNomContainingIgnoreCase(String search, Pageable pageable);
}

