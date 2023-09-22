package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

public interface LoanService {

    void saveLoan(Loan loan);

    Loan findById(Long id);

    LoanDTO getLoanDTO(Long id);

    Set<LoanDTO> getLoansDTO();

    Set<LoanDTO> getLoans(Authentication authentication);

    Loan getByName(String name);
}
