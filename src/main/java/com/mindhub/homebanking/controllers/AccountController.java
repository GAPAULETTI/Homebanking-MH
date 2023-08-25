package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
        @Autowired
        private AccountRepository accountRepository;

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

        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String currentUser = authentication.getName();
                //(@RequestBody Account newAccount, Authentication authentication)
        //( @RequestParam String number, @RequestParam LocalDate creationDate, @RequestParam double balance)
       // @PostMapping("clients/current/accounts")

        @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
        public ResponseEntity<Object> accountRegister (Authentication authentication ) {

                Client currentClient = repoClient.findByEmail(authentication.getName());
                System.out.println(currentClient);

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
                int number = (int)(Math.random()*99999999+1);
                return prefix + number;
        }

        /*
        @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
        @RequestParam String firstName, @RequestParam String lastName,
        @RequestParam String email, @RequestParam String password){

        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(repoClient.findByEmail(email) != null){
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        repoClient.save(new Client(firstName,lastName,email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

         */


}
