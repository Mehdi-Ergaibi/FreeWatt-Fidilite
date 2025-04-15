package com.Fidilite.FreeWatt.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Fidilite.FreeWatt.Entity.Achat;

@Repository
public interface AchatRepository extends JpaRepository<Achat, Long> {
    List<Achat> findByClientId(Long clientId);
}
