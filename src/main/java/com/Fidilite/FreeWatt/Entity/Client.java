package com.Fidilite.FreeWatt.Entity;

import java.time.LocalDateTime;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;
    private String telephone;

    private int totalPoints;

    private LocalDateTime derniereDateAchat;
    private double totalDepenses;

    @OneToMany(mappedBy = "client")
    private List<Achat> achats;

    @OneToMany(mappedBy = "client")
    private List<PointTransaction> transactions;
}
