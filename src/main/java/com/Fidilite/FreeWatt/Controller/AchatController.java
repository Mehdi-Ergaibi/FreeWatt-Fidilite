package com.Fidilite.FreeWatt.Controller;

import com.Fidilite.FreeWatt.Entity.Achat;
import com.Fidilite.FreeWatt.Service.AchatService;
import com.Fidilite.FreeWatt.dto.AchatDto;

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

    @PostMapping("/client/{clientId}")
    public ResponseEntity<Achat> addAchat(@PathVariable Long clientId, @RequestParam double montant) {
        Achat achat = achatService.addAchat(clientId, montant);
        return new ResponseEntity<>(achat, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AchatDto>> getAllAchats() {
        List<AchatDto> achats = achatService.getAllAchats();
        return new ResponseEntity<>(achats, HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AchatDto>> getAchatsByClient(@PathVariable Long clientId) {
        List<AchatDto> achats = achatService.getAchatsByClient(clientId);
        return new ResponseEntity<>(achats, HttpStatus.OK);
    }
}
