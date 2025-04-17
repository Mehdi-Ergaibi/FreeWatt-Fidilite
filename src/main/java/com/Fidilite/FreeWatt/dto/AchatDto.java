package com.Fidilite.FreeWatt.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AchatDto {
    private Long id;
    private double montant;
    private LocalDateTime date;
    private Long clientId;
}
