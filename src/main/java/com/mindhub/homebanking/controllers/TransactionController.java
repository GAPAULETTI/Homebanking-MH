package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction( @RequestParam String fromAccountNumber,@RequestParam String toAccountNumber,
                                                    @RequestParam double amount, @RequestParam String description,
                                                     Authentication authentication){
        Client currentClient = clientService.getByAuth(authentication);
        //Properties names in frontend (fromAccountNumber, toAccountNumber, amount, description)
        Account debitAccount = accountService.getAccountByNumber(fromAccountNumber);
        Account creditAccount = accountService.getAccountByNumber(toAccountNumber);

        if( description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || amount <= 0){
            return new ResponseEntity<>("You must complete all the required information",HttpStatus.FORBIDDEN);
        }

        if(debitAccount.getNumber() == creditAccount.getNumber()){
            return new ResponseEntity<>("The origin account cannot be same the destination account",HttpStatus.FORBIDDEN);
        }
        if(debitAccount.getBalance() < amount){
            return new ResponseEntity<>("non-sufficient funds", HttpStatus.FORBIDDEN);
        }
        if(debitAccount.getClient() != currentClient){
            return new ResponseEntity<>("The transfer cannot be performed by an unauthenticated client.", HttpStatus.FORBIDDEN);
        }


        if( debitAccount.getNumber() != null && creditAccount.getNumber() != null){
            Transaction debitTransaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDate.now());
            debitAccount.addTransaction(debitTransaction);
            transactionService.save(debitTransaction);
            debitAccount.setBalance(debitAccount.getBalance() - amount);
            accountService.saveAccount(debitAccount);

            Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description,LocalDate.now());
            creditAccount.addTransaction(creditTransaction);
            transactionService.save(creditTransaction);
            creditAccount.setBalance(creditAccount.getBalance() + amount);
            accountService.saveAccount(creditAccount);

            return new ResponseEntity<>("The transfer was successful ",HttpStatus.CREATED);

        }else{
            return new ResponseEntity<>("Account not found",HttpStatus.FORBIDDEN);
        }
        
    }
}
