package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction( @RequestParam String fromAccountNumber,@RequestParam String toAccountNumber,
                                                    @RequestParam double amount, @RequestParam String description,
                                                     Authentication authentication){
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        //Properties names in frontend (fromAccountNumber, toAccountNumber, amount, description)
        Account debitAccount = accountRepository.findByNumber(fromAccountNumber);
        Account creditAccount = accountRepository.findByNumber(toAccountNumber);


        if( description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(debitAccount.getNumber() == creditAccount.getNumber()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(debitAccount.getBalance() < amount){
            return new ResponseEntity<>("non-sufficient funds", HttpStatus.FORBIDDEN);
        }


        if( debitAccount.getNumber() != null && creditAccount.getNumber() != null && debitAccount.getClient() == currentClient){
            Transaction debitTransaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDate.now());
            debitAccount.addTransaction(debitTransaction);
            transactionRepository.save(debitTransaction);
            debitAccount.setBalance(debitAccount.getBalance() - amount);
            accountRepository.save(debitAccount);

            Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description,LocalDate.now());
            creditAccount.addTransaction(creditTransaction);
            transactionRepository.save(creditTransaction);
            creditAccount.setBalance(creditAccount.getBalance() + amount);
            accountRepository.save(creditAccount);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
    }
}
