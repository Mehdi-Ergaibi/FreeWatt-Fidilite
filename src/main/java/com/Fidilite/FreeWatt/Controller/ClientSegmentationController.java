package com.Fidilite.FreeWatt.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Fidilite.FreeWatt.Service.ClientSegmentationService;
import com.Fidilite.FreeWatt.dto.ClientDto;

@RestController
@RequestMapping("/clients/segmentation")
public class ClientSegmentationController {

    @Autowired
    private ClientSegmentationService segmentationService;

    @GetMapping("/niveaux-fidelite")
    public ResponseEntity<Map<String, Long>> getNiveauxFidelite() {
        return ResponseEntity.ok(segmentationService.getRepartitionFidelite());
    }

    @GetMapping("/top-clients/spending")
    public ResponseEntity<List<ClientDto>> getTopBySpending() {
        return ResponseEntity.ok(segmentationService.getTopClientsBySpending());
    }

    @GetMapping("/top-clients/frequency")
    public ResponseEntity<List<ClientDto>> getTopByFrequency() {
        return ResponseEntity.ok(segmentationService.getTopClientsByFrequency());
    }

    @GetMapping("/inactifs/{jours}")
    public ResponseEntity<List<ClientDto>> getClientsInactifs(@PathVariable int jours) {
        return ResponseEntity.ok(segmentationService.getClientsInactifs(jours));
    }

    @GetMapping("/clients-churn")
    public ResponseEntity<List<ClientDto>> getClientsChurn() {
        return ResponseEntity.ok(segmentationService.getChurnClients());
    }
}
