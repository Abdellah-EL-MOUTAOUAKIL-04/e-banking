package ma.abdellah.ebankingbackend.services;

import ma.abdellah.ebankingbackend.entities.BankAccount;
import ma.abdellah.ebankingbackend.entities.CurrentAccount;
import ma.abdellah.ebankingbackend.entities.Customer;
import ma.abdellah.ebankingbackend.entities.SavingAccount;
import ma.abdellah.ebankingbackend.exceptions.BalanceNotSufficientException;
import ma.abdellah.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.abdellah.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> bankAccountList();
}
