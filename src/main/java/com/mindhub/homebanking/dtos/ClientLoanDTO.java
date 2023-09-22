package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientLoanDTO {

    private long id;

    private double amount;
    private int payments;

    private String loanName;

    private double interest;

    private double paymentAmount;

    private double totalLoan;

    private LocalDate expirationDate;


    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.loanName = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.interest = clientLoan.getInterest();
        this.paymentAmount = clientLoan.getPaymentAmount();
        this.totalLoan = clientLoan.getTotalLoan();
        this.expirationDate = clientLoan.getExpirationDate();
    }


    public long getId() {
        return id;
    }

    public String getLoanName() {
        return loanName;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public double getInterest() {
        return interest;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public double getTotalLoan() {
        return totalLoan;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }
}
