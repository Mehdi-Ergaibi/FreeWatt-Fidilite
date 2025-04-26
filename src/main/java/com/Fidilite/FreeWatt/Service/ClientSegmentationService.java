package com.Fidilite.FreeWatt.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fidilite.FreeWatt.Entity.Achat;
import com.Fidilite.FreeWatt.Entity.Client;
import com.Fidilite.FreeWatt.Entity.PointTransaction;
import com.Fidilite.FreeWatt.dto.ClientDto;
import com.Fidilite.FreeWatt.repositories.ClientRepository;

@Service
public class ClientSegmentationService {

    @Autowired
    private ClientRepository clientRepository;

    public Map<String, Long> getRepartitionFidelite() {
        List<Client> clients = clientRepository.findAll();

        return clients.stream().collect(Collectors.groupingBy(
                client -> {
                    if (client.getTotalPoints() >= 5000)
                        return "Or";
                    else if (client.getTotalPoints() >= 1000)
                        return "Argent";
                    else
                        return "Bronze";
                },
                Collectors.counting()));
    }

    public List<ClientDto> getTopClientsBySpending() {
        List<Client> clients = clientRepository.findAllByOrderByTotalDepensesDesc();
        return clients.stream()
                .limit(10)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ClientDto> getTopClientsByFrequency() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .sorted((c1, c2) -> Integer.compare(
                        c2.getAchats().size(), c1.getAchats().size()))
                .limit(10)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ClientDto> getClientsInactifs(int jours) {
        LocalDateTime limite = LocalDateTime.now().minusDays(jours);
        return clientRepository.findAll().stream()
                .filter(c -> c.getDerniereDateAchat() != null && c.getDerniereDateAchat().isBefore(limite))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ClientDto> getChurnClients() {
        LocalDateTime limite = LocalDateTime.now().minusDays(30);
        return clientRepository.findAll().stream()
                .filter(c -> c.getDerniereDateAchat() != null
                        && c.getDerniereDateAchat().isBefore(limite)
                        && c.getAchats().size() >= 5)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ClientDto toDto(Client c) {
        return new ClientDto(
                c.getId(),
                c.getNom(),
                c.getEmail(),
                c.getTelephone(),
                c.getTotalPoints(),
                c.getTotalDepenses(),
                c.getAchats().stream().map(Achat::getId).collect(Collectors.toList()),
                c.getTransactions().stream().map(PointTransaction::getId).collect(Collectors.toList())
        );
    }
}
