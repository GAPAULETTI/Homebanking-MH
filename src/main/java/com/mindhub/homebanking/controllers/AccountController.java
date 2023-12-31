package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.CardType;
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

import static com.mindhub.homebanking.Utils.Util.generateNumberAccount;
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


        @GetMapping("/accounts")
        public List<AccountDTO> getAccounts(){
                return accountService.getAccounts();
        }
        @GetMapping("/accounts/{id}")
        public AccountDTO getAccountById(@PathVariable Long id){
                return accountService.getAccountDTO(id);
                //return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);

        }
        @GetMapping("/clients/current/accounts")
        public Set<AccountDTO> getCurrentAccount(Authentication authentication) {
                return accountService.getCurrentAccount(authentication);
        }

        @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
        public ResponseEntity<Object> accountRegister (Authentication authentication, @RequestParam AccountType accountType) {

                Client currentClient = clientService.getByAuth(authentication);
                List<Account> activeAccounts = currentClient.getAccounts().stream().filter(account -> account.isActiveAccount()==true).collect(toList());
                String numberAccount = generateNumberAccount();
                if(accountService.getAccountByNumber(numberAccount) != null){
                      return new ResponseEntity<>("This account number already exists", HttpStatus.FORBIDDEN);
                }
                if (activeAccounts.size() < 3) {

                        Account account = new Account(numberAccount, accountType ,LocalDate.now(), 0.0, true);
                        accountService.saveAccount(account);
                        currentClient.addAccount(account);

                        clientService.saveClient(currentClient);

                       return new ResponseEntity<>("The new account was created successfully",HttpStatus.CREATED);
              } else {
                      return new ResponseEntity<>("The maximum account number has been reached.",HttpStatus.FORBIDDEN);}

        }


        @PatchMapping("/clients/current/accounts")
        public ResponseEntity<Object> deleteAccount(@RequestParam String accountNumber, Authentication authentication){

                Client currentClient = clientService.getByAuth(authentication);
                Account accountToDelete = accountService.getAccountByNumber(accountNumber);
                List<Account> activeAccounts = currentClient.getAccounts().stream().filter(account -> account.isActiveAccount()==true).collect(toList());
                int activeAccount = activeAccounts.size();

                if(activeAccount == 1){
                        return new ResponseEntity<>("This account cannot be deleted", HttpStatus.FORBIDDEN);
                }
                if(accountToDelete.getBalance() > 0){
                        return new ResponseEntity<>("Accounts with available funds cannot be deleted", HttpStatus.FORBIDDEN);
                }
                if(accountNumber != null ){
                        accountToDelete.setActiveAccount(false);
                        accountService.saveAccount(accountToDelete);
                        clientService.saveClient(currentClient);

                   return new ResponseEntity<>("The account is banished", HttpStatus.OK);
                }
                return new ResponseEntity<>("This account cannot be deleted", HttpStatus.FORBIDDEN);
        }

}
