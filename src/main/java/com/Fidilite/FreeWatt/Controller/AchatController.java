package com.Fidilite.FreeWatt.Controller;

import com.Fidilite.FreeWatt.Entity.Achat;
import com.Fidilite.FreeWatt.Service.AchatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/achats")
public class AchatController {

    @Autowired
    private AchatService achatService;

    // Ajouter un achat
    @PostMapping("/client/{clientId}")
    public ResponseEntity<Achat> addAchat(@PathVariable Long clientId, @RequestParam double montant) {
        Achat achat = achatService.addAchat(clientId, montant);
        return new ResponseEntity<>(achat, HttpStatus.CREATED);
    }

    // Récupérer tous les achats
    @GetMapping
    public ResponseEntity<List<Achat>> getAllAchats() {
        List<Achat> achats = achatService.getAllAchats();
        return new ResponseEntity<>(achats, HttpStatus.OK);
    }

    // Récupérer les achats d'un client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Achat>> getAchatsByClient(@PathVariable Long clientId) {
        List<Achat> achats = achatService.getAchatsByClient(clientId);
        return new ResponseEntity<>(achats, HttpStatus.OK);
    }
}
