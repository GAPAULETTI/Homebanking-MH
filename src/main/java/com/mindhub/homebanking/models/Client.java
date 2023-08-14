package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class Client {

    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String firstName;
    private String lastName;
    private String email;

    //Link to Account
    @OneToMany(mappedBy="client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    //ClientLoan Relationship
    @OneToMany(mappedBy = "loanOwner", fetch = FetchType.EAGER)
    private Set<ClientLoan> loans = new HashSet<>();

    //Constructors
    public Client() {
    }

    public Client( String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    //Getters and Setters
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Get accounts
    public Set<Account> getAccounts() {
        return accounts;
    }

    //Method to Add Account
    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }

    public Set<ClientLoan> getLoans() {
        return loans;
    }

    public void addLoan(ClientLoan clientLoan){
        clientLoan.setLoanOwner(this);
        loans.add(clientLoan);
    }
}
