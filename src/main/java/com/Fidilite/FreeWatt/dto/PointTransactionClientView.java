package com.Fidilite.FreeWatt.dto;

import java.time.LocalDateTime;

import com.Fidilite.FreeWatt.Entity.PointTransaction;
import com.Fidilite.FreeWatt.type.PointTransactionType;

import lombok.Data;

@Data
public class PointTransactionClientView {
    private Long id;
    private double points;
    private PointTransactionType type;
    private String description;
    private LocalDateTime date;
    private String clientNom;

    public PointTransactionClientView(PointTransaction transaction) {
        this.id = transaction.getId();
        this.points = transaction.getPoints();
        this.type = transaction.getType();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.clientNom = transaction.getClient().getNom();
    }

}
