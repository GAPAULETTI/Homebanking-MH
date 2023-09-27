package com.mindhub.homebanking;

import com.mindhub.homebanking.Utils.Util;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository){


		return (args -> {


			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("12345"));
			Client client2 = new Client("Bruno", "Diaz","batmanpanzon@hero.com", passwordEncoder.encode("12345"));
			Client admin = new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("123456"));

			Account account1 = new Account("VIN001",AccountType.CURRENT, LocalDate.now(), 5000, true);
			Account account2 = new Account("VIN002",AccountType.SAVING, LocalDate.now().plusDays(1), 7500,true);
			Account account3 = new Account("VIN003",AccountType.CURRENT, LocalDate.now(), 3000,true);
			Account account4 = new Account("VIN004",AccountType.SAVING, LocalDate.now().minusWeeks(1), 9500,true);

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(admin);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);


			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);


			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 3500, "Reintegro Mastercard", LocalDate.now(), Util.updateCreditBalance(5000,3500));
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 3000,"Compra NYC",LocalDate.now(), Util.updateDebitBalance(7500,3000));

			Transaction transaction3 = new Transaction(TransactionType.DEBIT, 3500, "MovEntreCtas", LocalDate.now(), Util.updateDebitBalance(8500,3500));
			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.setBalance(7500 - 3000);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);

			accountRepository.save(account1);
			accountRepository.save(account2);

			Loan hipotecario = new Loan("Hipotecario", 500000, List.of(12,24,36,48,60),80);
			Loan personal = new Loan("Personal", 100000, List.of(6,12,24),60);
			Loan automotriz = new Loan("Automotriz", 300000, List.of(6,12,24,36),75);

			loanRepository.save(hipotecario);
			loanRepository.save(personal);
			loanRepository.save(automotriz);

			ClientLoan loan1 = new ClientLoan(400000, 60);
			ClientLoan loan2 = new ClientLoan(500000, 12);

			hipotecario.addLoan(loan1);
			client1.addLoan(loan1);

			personal.addLoan(loan2);
			client1.addLoan(loan2);


			clientLoanRepository.save(loan1);
			clientLoanRepository.save(loan2);



			Card card1 = new Card(client1.getFirstName()+" "+client1.getLastName(), CardType.DEBIT, CardColor.GOLD, "8909 2321 1232 9210", 321, LocalDate.now(), LocalDate.now().plusYears(5), true);
			Card card2 = new Card(client1.getFirstName()+" "+ client1.getLastName(), CardType.CREDIT, CardColor.TITANIUM,"8889 6576 5434 4323", 456, LocalDate.now().minusYears(6), LocalDate.now().minusYears(1), true);
			Card card3 = new Card(client2.getFirstName()+" "+client2.getLastName(),CardType.CREDIT, CardColor.SILVER,"9090 4343 3423 6565", 665, LocalDate.now(), LocalDate.now().plusYears(5), true);

			card1.setClient(client1);
			client1.addCard(card1);
			card2.setClient(client1);
			client1.addCard(card2);
			card3.setClient(client2);
			client2.addCard(card3);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

			clientRepository.save(client1);
			clientRepository.save(client2);







		});
	}


}
