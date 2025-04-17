package com.Fidilite.FreeWatt.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fidilite.FreeWatt.Entity.Achat;
import com.Fidilite.FreeWatt.Entity.Client;
import com.Fidilite.FreeWatt.Entity.PointTransaction;
import com.Fidilite.FreeWatt.dto.ClientDto;
import com.Fidilite.FreeWatt.repositories.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client addClient(Client client) {

        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("Un client avec cet email existe déjà.");
        }

        return clientRepository.save(client);
    }

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(c -> new ClientDto(
                        c.getId(),
                        c.getNom(),
                        c.getEmail(),
                        c.getTelephone(),
                        c.getTotalPoints(),
                        c.getAchats().stream()
                                .map(Achat::getId)
                                .collect(Collectors.toList()),
                        c.getTransactions().stream()
                                .map(PointTransaction::getId)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Optional<Client> getClientByEmail(String email) {
        if (clientRepository.existsByEmail(email)) {
            return clientRepository.findByEmail(email);
        }

        throw new RuntimeException("Un client avec cet email existe déjà.");
    }

    public Client updateClient(Long clientId, Client clientDetails) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        client.setNom(clientDetails.getNom());
        client.setEmail(clientDetails.getEmail());
        client.setTotalPoints(clientDetails.getTotalPoints());
        return clientRepository.save(client);
    }

    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }
}
