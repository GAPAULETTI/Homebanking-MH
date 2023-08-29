package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableMap;


@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository repoClient;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
         return repoClient.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id){
        return repoClient.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> newClient(
        @RequestParam String firstName, @RequestParam String lastName,
        @RequestParam String email, @RequestParam String password, AccountController account){

        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(repoClient.findByEmail(email) != null){
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName,lastName,email, passwordEncoder.encode(password));
       newClient.addAccount(accountRepository.save(new Account(account.numberAccount(), LocalDate.now(), 0.0)));
        repoClient.save(newClient);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/clients/current")
    public ClientDTO getByAuth(Authentication auth){
        Client clientCurrent  = repoClient.findByEmail(auth.getName());
        return new ClientDTO(clientCurrent);
    }




}






