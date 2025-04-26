package com.Fidilite.FreeWatt.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public List<ClientDto> getClientsWithoutPagination() {
        List<Client> cls = clientRepository.findAll();
        return cls.stream()
                .map(c -> new ClientDto(
                    c.getId(),
                    c.getNom(),
                    c.getEmail(),
                    c.getTelephone(),
                    c.getTotalPoints(),
                    c.getTotalDepenses(),
                    c.getAchats().stream().map(Achat::getId).collect(Collectors.toList()),
                    c.getTransactions().stream().map(PointTransaction::getId).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
    

    public Page<ClientDto> getClients(Pageable pageable, String search) {
        Page<Client> clients;
        if (search != null && !search.isEmpty()) {
            clients = clientRepository.findByNomContainingIgnoreCase(search, pageable);
        } else {
            clients = clientRepository.findAll(pageable);
        }

        return clients.map(c -> new ClientDto(
                c.getId(),
                c.getNom(),
                c.getEmail(),
                c.getTelephone(),
                c.getTotalPoints(),
                c.getTotalDepenses(),
                c.getAchats().stream().map(Achat::getId).collect(Collectors.toList()),
                c.getTransactions().stream().map(PointTransaction::getId).collect(Collectors.toList())));
    }

    public Optional<ClientDto> getClientById(Long id) {
        return clientRepository.findById(id)
                .map(c -> new ClientDto(
                        c.getId(),
                        c.getNom(),
                        c.getEmail(),
                        c.getTelephone(),
                        c.getTotalPoints(),
                        c.getTotalDepenses(),
                        c.getAchats().stream()
                                .map(Achat::getId)
                                .collect(Collectors.toList()),
                        c.getTransactions().stream()
                                .map(PointTransaction::getId)
                                .collect(Collectors.toList())));
    }
    public Page<ClientDto> searchClients(String query, Pageable pageable) {
        return clientRepository.findByNomContainingIgnoreCase(query, pageable)
            .map(c -> new ClientDto(
                c.getId(),
                        c.getNom(),
                        c.getEmail(),
                        c.getTelephone(),
                        c.getTotalPoints(),
                        c.getTotalDepenses(),
                        c.getAchats().stream()
                                .map(Achat::getId)
                                .collect(Collectors.toList()),
                        c.getTransactions().stream()
                                .map(PointTransaction::getId)
                                .collect(Collectors.toList())));
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
