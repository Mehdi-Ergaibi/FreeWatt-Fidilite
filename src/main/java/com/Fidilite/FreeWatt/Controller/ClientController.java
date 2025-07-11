package com.Fidilite.FreeWatt.Controller;

import com.Fidilite.FreeWatt.Entity.Client;
import com.Fidilite.FreeWatt.Entity.MoneyTransaction;
import com.Fidilite.FreeWatt.Service.ClientService;
import com.Fidilite.FreeWatt.Service.PointTransactionService;
import com.Fidilite.FreeWatt.dto.ClientDto;
import com.Fidilite.FreeWatt.repositories.MoneyTransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PointTransactionService pointTransactionService;

    @Autowired
    private MoneyTransactionRepository moneyTransactionRepository;

    @PostMapping("/{clientId}/convert-points")
    public ResponseEntity<String> convertPointsToMoney(@PathVariable Long clientId) {
        try {
            double money = pointTransactionService.convertPointsToMoney(clientId);
            return ResponseEntity.ok("Conversion réussie ! Montant : " + money + " Dh");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{clientId}/money-transactions")
    public List<MoneyTransaction> getMoneyTransactions(@PathVariable Long clientId) {
        return moneyTransactionRepository.findByClientId(clientId);
    }

    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        Client newClient = clientService.addClient(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> getClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ClientDto> clientPage = clientService.getClients(pageable, search);
        return ResponseEntity.ok(clientPage);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClientDto>> getClientsWithoutPagination(
            ) {

        List<ClientDto> clientPage = clientService.getClientsWithoutPagination();
        return ResponseEntity.ok(clientPage);
    }

    // Récupérer un client par ID
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        Optional<ClientDto> client = clientService.getClientById(id);
        return client.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Récupérer un client par email
    @GetMapping("/email/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
        Optional<Client> client = clientService.getClientByEmail(email);
        return client.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Mettre à jour un client
    @PutMapping("/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable Long clientId, @RequestBody Client clientDetails) {
        Client updatedClient = clientService.updateClient(clientId, clientDetails);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    // Supprimer un client
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ClientDto>> searchClients(
            @RequestParam String query,
            @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(clientService.searchClients(query, pageable));
    }
}
