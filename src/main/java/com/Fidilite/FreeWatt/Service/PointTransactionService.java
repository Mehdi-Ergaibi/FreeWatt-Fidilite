package com.Fidilite.FreeWatt.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fidilite.FreeWatt.Entity.Client;
import com.Fidilite.FreeWatt.Entity.MoneyTransaction;
import com.Fidilite.FreeWatt.Entity.PointTransaction;
import com.Fidilite.FreeWatt.dto.PointTransactionClientView;
import com.Fidilite.FreeWatt.repositories.ClientRepository;
import com.Fidilite.FreeWatt.repositories.MoneyTransactionRepository;
import com.Fidilite.FreeWatt.repositories.PointTransactionRepository;
import com.Fidilite.FreeWatt.type.PointTransactionType;

@Service
public class PointTransactionService {

    @Autowired
    private PointTransactionRepository pointTransactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private MoneyTransactionRepository moneyTransactionRepository;

    public double convertPointsToMoney(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        int totalPoints = client.getTotalPoints();

        int seuilConversion = parameterService.getSeuilConversion();

        if (totalPoints < seuilConversion) {
            throw new RuntimeException("Le client n'a pas assez de points pour effectuer la conversion.");
        }

        double conversionRate = parameterService.getConversionRate();

        double moneyEquivalent = totalPoints / conversionRate;

        client.setTotalPoints(0);
        clientRepository.save(client);

        MoneyTransaction moneyTransaction = new MoneyTransaction(client, moneyEquivalent, "Points converted to money");
        moneyTransactionRepository.save(moneyTransaction);

        createPointTransaction(client, -totalPoints, PointTransactionType.DEPENSE);

        return moneyEquivalent;
    }

    public PointTransaction createPointTransaction(Client client, double points, PointTransactionType type) {

        PointTransaction transaction = new PointTransaction();
        transaction.setClient(client);
        transaction.setPoints((int) points);
        transaction.setType(type);
        transaction.setDate(LocalDateTime.now());

        transaction.setDescription("Transaction de points: " + type.name());

        return pointTransactionRepository.save(transaction);
    }

    public List<PointTransactionClientView> getPointTransactionsByClient(Long clientId) {
        List<PointTransaction> transactions = pointTransactionRepository.findByClientId(clientId);
        return transactions.stream()
                .map(PointTransactionClientView::new)
                .collect(Collectors.toList());
    }

    public List<PointTransactionClientView> getAllPointTransactions() {
        return pointTransactionRepository.findAll().stream()
            .map(PointTransactionClientView::new)
            .collect(Collectors.toList());
    }
    
}
