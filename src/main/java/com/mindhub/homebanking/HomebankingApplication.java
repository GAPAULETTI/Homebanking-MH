package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository repoTransaction, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository){
		return (args -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Bruno", "Diaz","batmanpanzon@hero.com");


			Account account1 = new Account("VIN001", LocalDate.now(), 5000);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
			Account account3 = new Account("VIN003", LocalDate.now(), 3000);
			Account account4 = new Account("VIN004", LocalDate.now().minusWeeks(1), 9500);

			clientRepository.save(client1);
			clientRepository.save(client2);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);


			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			Transaction transaction1 = new Transaction(TransactionType.CREDITO, 3500, "Reintegro Mastercard", LocalDate.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBITO, -3000,"Compra NYC",LocalDate.now());
			Transaction transaction3 = new Transaction(TransactionType.DEBITO, 4000, "MovEntreCtas", LocalDate.now());
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction2);

			repoTransaction.save(transaction1);
			repoTransaction.save(transaction2);
			repoTransaction.save(transaction3);

			Loan hipotecario = new Loan("Hipotecario", 500000, List.of(12,24,36,48,60));
			Loan personal = new Loan("Personal", 100000, List.of(6,12,24));
			Loan automotriz = new Loan("Automotriz", 300000, List.of(6,12,24,36));

			loanRepository.save(hipotecario);
			loanRepository.save(personal);
			loanRepository.save(automotriz);

			ClientLoan loan1 = new ClientLoan(400000, 60, client1, hipotecario);
			ClientLoan loan2 = new ClientLoan(50000, 12, client1, personal);

			ClientLoan loan3 = new ClientLoan(100000, 24, client2, personal);
			ClientLoan loan4 = new ClientLoan(200000, 36, client2,automotriz);
			clientLoanRepository.save(loan1);
			clientLoanRepository.save(loan2);
			clientLoanRepository.save(loan3);
			clientLoanRepository.save(loan4);

		});
	}


}
