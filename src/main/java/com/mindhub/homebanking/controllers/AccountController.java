package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
        @Autowired
        private AccountRepository accountRepository;
        @Autowired
        private AccountService accountService;
        @Autowired
        private ClientService clientService;

        @Autowired
        private ClientRepository repoClient;


        @RequestMapping("/accounts")
        public List<AccountDTO> getAccounts(){
                return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
        }
        @RequestMapping("/accounts/{id}")
        public AccountDTO getAccountById(@PathVariable Long id){
                return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);

        }
        @GetMapping("/clients/current/accounts")
        public Set<AccountDTO> getCurrentAccount(Authentication authentication){
              return accountService.getCurrentAccount(authentication)  ;
        }

        @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
        public ResponseEntity<Object> accountRegister (Authentication authentication ) {

                Client currentClient = clientService.getByAuth(authentication);

                if (currentClient.getAccounts().size() < 3) {

                        Account account = new Account(numberAccount(), LocalDate.now(),0.0);
                        currentClient.addAccount(accountRepository.save(account));
                        repoClient.save(currentClient);

                       return new ResponseEntity<>("The new account was created successfully",HttpStatus.CREATED);
              } else {
                      return new ResponseEntity<>(HttpStatus.FORBIDDEN);}

        }


        public String numberAccount(){
                String prefix = "VIN-";
                int number = (int)(10000000 + (Math.random()*89999999));
                return prefix + number;
        }



}
