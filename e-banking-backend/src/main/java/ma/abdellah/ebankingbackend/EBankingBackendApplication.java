package ma.abdellah.ebankingbackend;

import ma.abdellah.ebankingbackend.dtos.CustomerDTO;
import ma.abdellah.ebankingbackend.entities.*;
import ma.abdellah.ebankingbackend.enums.AccountStatus;
import ma.abdellah.ebankingbackend.enums.OperationType;
import ma.abdellah.ebankingbackend.exceptions.BalanceNotSufficientException;
import ma.abdellah.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.abdellah.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.abdellah.ebankingbackend.repositories.AccountOperationRepository;
import ma.abdellah.ebankingbackend.repositories.BankAccountRepository;
import ma.abdellah.ebankingbackend.repositories.CustomerRepository;
import ma.abdellah.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankingBackendApplication.class, args);
	}



	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository) {
		return args -> {
			Stream.of("abdellah", "ahmed", "salah").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(c -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random() * 9000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(c);
				currentAccount.setOverDraft(10000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random() * 9000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(c);
				savingAccount.setInterestRate(2.4);
				bankAccountRepository.save(savingAccount);
			});
			bankAccountRepository.findAll().forEach(a -> {
				for (int i = 0; i < 10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random() * 1200);
					accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAccount(a);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
		return args -> {
			Stream.of("abdellah","yasser","othmane").forEach(name -> {
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setName(name);
				customerDTO.setEmail(name + "@gmail.com");
				bankAccountService.saveCustomer(customerDTO);
			});
			bankAccountService.listCustomers().forEach(c -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000, c.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*12000, 5.5, c.getId());
					List<BankAccount> bankAccounts = bankAccountService.bankAccountList();
					for(BankAccount bankAccount:bankAccounts){
						for (int i = 0; i < 10; i++) {
							bankAccountService.credit(bankAccount.getId(), 10000 + Math.random() * 12000, "Credit");
							bankAccountService.debit(bankAccount.getId(), 1000 + Math.random() * 1200, "Debit");
						}
					}
				} catch (CustomerNotFoundException | BankAccountNotFoundException | BalanceNotSufficientException e) {
                    throw new RuntimeException(e);
                }

            });
		};
	}
}
