package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name= "native", strategy = "native")
    private long id;

    private String name;
    private double maxAmount;


    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> payments;

    @OneToMany(mappedBy = "loanClients", fetch = FetchType.EAGER)
    private Set<ClientLoan> loanClients = new HashSet<>();

    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getLoanClients() {
        return loanClients;
    }

    public void setLoanClients(Set<ClientLoan> loanClients) {
        this.loanClients = loanClients;
    }
    public void addLoanClient(ClientLoan loanCLient){
        loanCLient.setLoanClients(this);
        loanClients.add(loanCLient);
    }
}
