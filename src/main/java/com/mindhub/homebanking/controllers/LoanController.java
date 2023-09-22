package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Utils.Util;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
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
import java.text.DecimalFormat;
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

    @GetMapping("/loans/{id}")
    public LoanDTO getLoanById(@PathVariable Long id){
        return loanService.getLoanDTO(id);
    }
    @GetMapping("/clients/current/loans")
    public Set<ClientLoanDTO> getCurrentLoans(Authentication authentication){
        Client currentClient = clientService.getByAuth(authentication);
        return currentClient.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toSet());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                             Authentication authentication) {

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

            int payments = loanApplicationDTO.getPayments();
            double interestLoan = requestedLoan.getInterestLoan();
            double amount = loanApplicationDTO.getAmount();



            double paymentAmount = Util.calculateInterest(payments, interestLoan,amount);
            //df.format(paymentAmount);
            //double feeAmount = Double.parseDouble((df.format(paymentAmount)));


            double accountBalance = toAccountNumber.getBalance();
            double totalLoan = loanApplicationDTO.getAmount() + Util.calculateInterest(payments, interestLoan,amount);
            System.out.println(totalLoan);

            ClientLoan creditLoan = new ClientLoan(requestedLoan.getName(),loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments(),
                    interestLoan, paymentAmount, totalLoan, LocalDate.now().plusMonths(1));
            clientLoanRepository.save(creditLoan);
            currentClient.addLoan(creditLoan);
            requestedLoan.addLoan(creditLoan);

            Transaction accreditedLoan = new Transaction(TransactionType.CREDIT, amount, (requestedLoan.getName() + " <<loan approved>> "), LocalDate.now(), Util.updateCreditBalance(accountBalance, amount));
            toAccountNumber.addTransaction(accreditedLoan);
            transactionService.save(accreditedLoan);
            toAccountNumber.setBalance(accountBalance + amount);
            accountService.saveAccount(toAccountNumber);
            //clientService.saveClient(currentClient);

            return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("This account does not belong to the current client", HttpStatus.FORBIDDEN);
        }
    }
}
