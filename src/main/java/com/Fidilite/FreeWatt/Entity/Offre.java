package com.Fidilite.FreeWatt.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private int pointsNecessaires;

    public Offre(String titre, String description, int pointsNecessaires) {
        this.titre = titre;
        this.description = description;
        this.pointsNecessaires = pointsNecessaires;
    }

}
