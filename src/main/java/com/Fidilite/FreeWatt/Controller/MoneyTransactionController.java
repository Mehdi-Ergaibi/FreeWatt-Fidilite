package com.Fidilite.FreeWatt.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Fidilite.FreeWatt.Service.MoneyTransactionService;
import com.Fidilite.FreeWatt.dto.MoneyTransactionClientView;

@RestController
@RequestMapping("/money-transactions")
public class MoneyTransactionController {

    @Autowired
    private MoneyTransactionService moneyTransactionService;

    @GetMapping
    public ResponseEntity<List<MoneyTransactionClientView>> getAllMoneyTransactions() {
        List<MoneyTransactionClientView> transactions = moneyTransactionService.getAllMoneyTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<MoneyTransactionClientView>> getMoneyTransactionsByClient(@PathVariable Long clientId) {
        List<MoneyTransactionClientView> transactions = moneyTransactionService.getMoneyTransactionsByClient(clientId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}