package com.Fidilite.FreeWatt.dto;

import java.time.LocalDateTime;

import com.Fidilite.FreeWatt.Entity.MoneyTransaction;

import lombok.Data;

@Data
public class MoneyTransactionClientView {
    private Long id;
    private double amount;
    private LocalDateTime date;
    private String description;
    private String clientNom;

    public MoneyTransactionClientView(MoneyTransaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
        this.clientNom = transaction.getClient().getNom();
    }
}
