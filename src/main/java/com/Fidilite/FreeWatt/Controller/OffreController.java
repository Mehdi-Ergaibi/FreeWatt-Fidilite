package com.Fidilite.FreeWatt.Controller;

import com.Fidilite.FreeWatt.Entity.Offre;
import com.Fidilite.FreeWatt.Service.OffreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offres")
public class OffreController {

    @Autowired
    private OffreService offreService;

    @PostMapping
    public ResponseEntity<Offre> addOffre(@RequestParam String titre,
            @RequestParam int pointsNecessaires,
            @RequestParam String description) {
        Offre offre = offreService.addOffre(titre, pointsNecessaires, description);
        return new ResponseEntity<>(offre, HttpStatus.CREATED);
    }

    // Mettre à jour une offre
    @PutMapping("/{offreId}")
    public ResponseEntity<Offre> updateOffre(@PathVariable Long offreId,
            @RequestParam String titre,
            @RequestParam String description,
            @RequestParam int pointsNecessaires) {
        Offre updatedOffre = offreService.updateOffre(offreId, titre, description, pointsNecessaires);
        return new ResponseEntity<>(updatedOffre, HttpStatus.OK);
    }

    // Supprimer une offre
    @DeleteMapping("/{offreId}")
    public ResponseEntity<Void> deleteOffre(@PathVariable Long offreId) {
        offreService.deleteOffre(offreId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Récupérer toutes les offres
    @GetMapping
    public ResponseEntity<List<Offre>> getAllOffres() {
        List<Offre> offres = offreService.getAllOffres();
        return new ResponseEntity<>(offres, HttpStatus.OK);
    }

    // Récupérer une offre par ID
    @GetMapping("/{offreId}")
    public ResponseEntity<Offre> getOffreById(@PathVariable Long offreId) {
        Offre offre = offreService.getOffreById(offreId);
        return new ResponseEntity<>(offre, HttpStatus.OK);
    }

    @PutMapping("/{offreId}/send-to-clients")
    public ResponseEntity<Void> sendOfferToClients(@PathVariable Long offreId) {
        Offre offre = offreService.getOffreById(offreId);
        offreService.envoyerOffreAClients(offre);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
