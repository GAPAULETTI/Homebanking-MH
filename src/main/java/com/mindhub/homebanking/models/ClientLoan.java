package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private double amount;
    private int payments;

    //Client Relationship
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client clientLoans;

    //Loan Relationship
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loanClients;

    public ClientLoan() {
    }

    public ClientLoan(double amount, int payments, Client clientLoans, Loan loanClients) {
        this.amount = amount;
        this.payments = payments;
        this.clientLoans = clientLoans;
        this.loanClients = loanClients;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }
    @JsonIgnore
    public Client getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Client clientLoans) {
        this.clientLoans = clientLoans;
    }
    @JsonIgnore
    public Loan getLoanClients() {
        return loanClients;
    }

    public void setLoanClients(Loan loanClients) {
        this.loanClients = loanClients;
    }

}
