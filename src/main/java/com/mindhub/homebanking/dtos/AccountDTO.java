package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;

    private String number;
    private LocalDate creationDate;
    private double balance;

    private boolean activeAccount;

    private AccountType accountType;

    private Set<TransactionDTO> transactions = new HashSet<>();


    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.accountType = account.getAccountType();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.activeAccount = account.isActiveAccount();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());

    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActiveAccount() {
        return activeAccount;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

}
