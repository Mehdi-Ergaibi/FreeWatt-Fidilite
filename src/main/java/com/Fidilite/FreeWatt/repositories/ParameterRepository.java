package com.Fidilite.FreeWatt.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Fidilite.FreeWatt.Entity.Parameter;
import com.Fidilite.FreeWatt.type.ParameterType;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    Optional<Parameter> findByType(ParameterType type);
}

