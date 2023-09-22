package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.List;

public class LoanDTO {

    private long id;

    private String name;

    private double maxAmount;

    private List<Integer> payments;

    private double interestLoan;

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.interestLoan = loan.getInterestLoan();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public double getInterestLoan() {
        return interestLoan;
    }
}
