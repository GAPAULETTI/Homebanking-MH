package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cache.annotation.AbstractCachingConfiguration;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {
    /*
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }
    @Test
    public void existClientLoan(){

        List<Loan> loans= loanRepository.findAll();
        assertThat(loans,hasItem(hasProperty("name",is("Hipotecario"))));
    }
    //Account Test
    @Test
    public void existAccounts(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));

    }
    @Test
    public void checkNumberAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number",is("VIN002"))));

    }
    //Card Test
    @Test
    public void existsCard(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }
    @Test
    public void checkCardProperty(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("id", is(not(0)))));
    }
    //Client test
    @Test
    public void existsClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }
    @Test
    public void checkClientAdmin(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("email", is( "admin@admin.com"))));
    }
    //Transaction test
    @Test
    public void existsTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }
    @Test
    public void checkTransactionData(){
       List<Transaction> transactions = transactionRepository.findAll();
       assertThat(transactions, hasItem(hasProperty("amount", is(not(0)))));
    }



     */


}
