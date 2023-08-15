package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository repoClient;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
         return repoClient.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }
    @RequestMapping("clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id){

        return repoClient.findById(id).map(client -> new ClientDTO(client)).orElse(null);

    }



}
