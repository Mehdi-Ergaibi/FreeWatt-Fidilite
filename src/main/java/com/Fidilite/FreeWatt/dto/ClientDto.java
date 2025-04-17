package com.Fidilite.FreeWatt.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ClientDto {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private int totalPoints;
    private List<Long> acahtID;
    private List<Long> transactionID;
}
