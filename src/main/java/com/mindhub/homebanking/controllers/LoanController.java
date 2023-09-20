package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Utils.Util;
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

import javax.transaction.Transactional;
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
            return loanService.getLoansDTO();
    }
    @Transactional
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
            return new ResponseEntity<>("You must choose an destination account",HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount() > requestedLoan.getMaxAmount()){return new ResponseEntity<>("This amount exceeds the maximum available",HttpStatus.FORBIDDEN);}

        if(currentClient.getAccounts().contains(toAccountNumber) == true) {

            double accountBalance = toAccountNumber.getBalance();
            double loanAmount = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.2);

            ClientLoan creditLoan = new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments());
            clientLoanRepository.save(creditLoan);
            currentClient.addLoan(creditLoan);
            requestedLoan.addLoan(creditLoan);

            Transaction accreditedLoan = new Transaction(TransactionType.CREDIT, loanAmount, (requestedLoan.getName() + " <<loan approved>> "), LocalDate.now(), Util.updateCreditBalance(accountBalance, loanAmount));
            toAccountNumber.addTransaction(accreditedLoan);
            transactionService.save(accreditedLoan);
            toAccountNumber.setBalance(accountBalance + loanAmount);
            accountService.saveAccount(toAccountNumber);
            //clientService.saveClient(currentClient);

            return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("This account does not belong to the current client", HttpStatus.FORBIDDEN);
        }
    }
}
