package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LoanService {

    void saveLoan(Loan loan);

    Loan findById(Long id);

    LoanDTO getById(Long id);

    List<LoanDTO> getLoansDTO();

    Loan getByName(String name);
}
