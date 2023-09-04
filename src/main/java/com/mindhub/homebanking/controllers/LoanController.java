package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @GetMapping("/loans")
    public Set<LoanDTO> getLoans(){
            return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }

    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                             Authentication authentication, Loan loan) {

        Client currentClient = clientService.getByAuth(authentication);
        Account toAccountNumber = accountService.getAccountByNumber(loanApplicationDTO.getToAccountNumber());
        Loan requestedLoan = loanService.findById(loanApplicationDTO.getLoanId());
        //Properties amount, payments, toAccountNumber, loanTypeId
        if (loanApplicationDTO.getAmount() <= 0 && loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("This parameters are not permit ", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getToAccountNumber().isEmpty()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount() < loan.getMaxAmount()){return new ResponseEntity<>(HttpStatus.FORBIDDEN);}


        ClientLoan loan1 = new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments());
        clientLoanRepository.save(loan1);

        Transaction accreditedLoan = new Transaction(TransactionType.CREDIT, (loanApplicationDTO.getAmount() + loanApplicationDTO.getAmount()*0.2) , ("Loan Type: " + loan.getName() + "Loan approved"), LocalDate.now());
        transactionService.save(accreditedLoan);
        toAccountNumber.setBalance(toAccountNumber.getBalance() + loanApplicationDTO.getAmount());
        accountService.saveAccount(toAccountNumber);
        currentClient.addLoan(loan1);
        clientService.saveClient(currentClient);



        return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
    }
}
