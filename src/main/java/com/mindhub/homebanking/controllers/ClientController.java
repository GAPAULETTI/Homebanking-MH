package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.mindhub.homebanking.Utils.Util.generateNumberAccount;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableMap;


@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository repoClient;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
         return clientService.getClientsDTO();
    }
    @GetMapping("/clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }
    @GetMapping("/clients/current")
    public ClientDTO getByAuth(Authentication authentication){
        return new ClientDTO(clientService.getByAuth(authentication));
    }
    @PostMapping("/clients")
    public ResponseEntity<Object> register(
        @RequestParam String firstName, @RequestParam String lastName,
        @RequestParam String email, @RequestParam String password){

        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }


        if(repoClient.findByEmail(email) != null){
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }


        if(accountService.getAccountByNumber(generateNumberAccount()) != null){
           return new ResponseEntity<>("This account number already exists", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName,lastName,email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);
        Account account = new Account(generateNumberAccount(), LocalDate.now(), 0.0);
        accountService.saveAccount(account);
        newClient.addAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}






