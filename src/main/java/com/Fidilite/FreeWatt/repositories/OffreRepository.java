package com.Fidilite.FreeWatt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Fidilite.FreeWatt.Entity.Offre;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {
}

