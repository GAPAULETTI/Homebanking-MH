package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {


    void saveClient(Client client);

    List<ClientDTO> getClientsDTO();

    ClientDTO getClientDTO(Long id);

    Client findById(Long id);

    Client getByAuth(Authentication authentication);
}
