package com.Fidilite.FreeWatt.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fidilite.FreeWatt.dto.MoneyTransactionClientView;
import com.Fidilite.FreeWatt.repositories.MoneyTransactionRepository;

@Service
public class MoneyTransactionService {

    @Autowired
    private MoneyTransactionRepository moneyTransactionRepository;

    /*
     * @Autowired
     * private ClientRepository clientRepository;
     */

    public List<MoneyTransactionClientView> getMoneyTransactionsByClient(Long clientId) {
        return moneyTransactionRepository.findByClientId(clientId).stream()
                .map(MoneyTransactionClientView::new)
                .collect(Collectors.toList());
    }

    public List<MoneyTransactionClientView> getAllMoneyTransactions() {
        return moneyTransactionRepository.findAll().stream()
                .map(MoneyTransactionClientView::new)
                .collect(Collectors.toList());
    }
}
