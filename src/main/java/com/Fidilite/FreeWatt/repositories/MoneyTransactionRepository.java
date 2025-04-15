package com.Fidilite.FreeWatt.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Fidilite.FreeWatt.Entity.MoneyTransaction;

public interface MoneyTransactionRepository extends JpaRepository<MoneyTransaction, Long> {
    List<MoneyTransaction> findByClientId(Long clientId);
}