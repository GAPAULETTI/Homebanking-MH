package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface AccountService {

    void saveAccount(Account account);



    AccountDTO getAccountDTO(Long id);

    Account getAccountByNumber(String number);

    Account findById(Long id);

    Set<AccountDTO> getCurrentAccount(Authentication authentication);
}
