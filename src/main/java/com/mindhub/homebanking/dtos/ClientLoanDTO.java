package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.HashSet;
import java.util.Set;

public class ClientLoanDTO {

    private long id;

    private double amount;
    private int payments;

    private Client loansClient;

    private Loan loans;





    public ClientLoanDTO() {
    }
    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();




    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public Client getLoansClient() {
        return loansClient;
    }

    public Loan getLoans() {
        return loans;
    }


}
