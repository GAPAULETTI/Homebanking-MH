package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {
    void save(Transaction transaction);

    Transaction findById(Long id);

    TransactionDTO getById(Long id);

    List<TransactionDTO> getTransactionDTO();
}
