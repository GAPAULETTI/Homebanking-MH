package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientService clientService;

    @Override
    public void saveLoan(Loan loan) {
         loanRepository.save(loan);
    }

    @Override
    public Loan findById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public LoanDTO getLoanDTO(Long id) {
       return loanRepository.findById(id).map(LoanDTO::new).orElse(null);
    }

    @Override
    public Set<LoanDTO> getLoansDTO() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }

    @Override
    public Set<LoanDTO> getLoans(Authentication authentication) {
        Client currentClient = clientService.getByAuth(authentication);
        return currentClient.getLoans().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }


    @Override
    public Loan getByName(String name) {
        return loanRepository.getByName(name);
    }
}
