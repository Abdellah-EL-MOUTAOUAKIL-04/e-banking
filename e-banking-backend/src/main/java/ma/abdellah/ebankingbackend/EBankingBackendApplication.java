package ma.abdellah.ebankingbackend;

import ma.abdellah.ebankingbackend.entities.AccountOperation;
import ma.abdellah.ebankingbackend.entities.CurrentAccount;
import ma.abdellah.ebankingbackend.entities.Customer;
import ma.abdellah.ebankingbackend.entities.SavingAccount;
import ma.abdellah.ebankingbackend.enums.AccountStatus;
import ma.abdellah.ebankingbackend.enums.OperationType;
import ma.abdellah.ebankingbackend.repositories.AccountOperationRepository;
import ma.abdellah.ebankingbackend.repositories.BankAccounRepository;
import ma.abdellah.ebankingbackend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankingBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccounRepository bankAccounRepository,
							AccountOperationRepository accountOperationRepository) {
		return args -> {
			Stream.of("abdellah","ahmed","salah").forEach(name->{
				Customer customer= new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(c->{
				CurrentAccount currentAccount= new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*9000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(c);
				currentAccount.setOverDraft(10000);
				bankAccounRepository.save(currentAccount);

				SavingAccount savingAccount= new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*9000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(c);
				savingAccount.setInterestRate(2.4);
				bankAccounRepository.save(savingAccount);
			});
			bankAccounRepository.findAll().forEach(a->{
				for (int i=0; i<10; i++){
					AccountOperation accountOperation= new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*1200);
					accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
					accountOperation.setBankAccount(a);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	}
}
