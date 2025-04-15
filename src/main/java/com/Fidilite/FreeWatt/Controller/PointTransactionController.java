package com.Fidilite.FreeWatt.Controller;

import com.Fidilite.FreeWatt.Entity.PointTransaction;
import com.Fidilite.FreeWatt.Service.PointTransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point-transactions")
public class PointTransactionController {

    @Autowired
    private PointTransactionService pointTransactionService;

    // Récupérer toutes les transactions de points
    @GetMapping
    public ResponseEntity<List<PointTransaction>> getAllPointTransactions() {
        List<PointTransaction> transactions = pointTransactionService.getAllPointTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // Récupérer les transactions de points d'un client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PointTransaction>> getPointTransactionsByClient(@PathVariable Long clientId) {
        List<PointTransaction> transactions = pointTransactionService.getPointTransactionsByClient(clientId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
