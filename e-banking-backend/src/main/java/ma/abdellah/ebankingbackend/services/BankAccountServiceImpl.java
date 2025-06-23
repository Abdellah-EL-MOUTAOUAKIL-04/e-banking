package ma.abdellah.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.abdellah.ebankingbackend.dtos.*;
import ma.abdellah.ebankingbackend.entities.*;
import ma.abdellah.ebankingbackend.enums.AccountStatus;
import ma.abdellah.ebankingbackend.enums.OperationType;
import ma.abdellah.ebankingbackend.exceptions.BalanceNotSufficientException;
import ma.abdellah.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.abdellah.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.abdellah.ebankingbackend.mappers.BankAccountMapperImpl;
import ma.abdellah.ebankingbackend.repositories.AccountOperationRepository;
import ma.abdellah.ebankingbackend.repositories.BankAccountRepository;
import ma.abdellah.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("[CREATE_CUSTOMER] Saving new customer: {}", customerDTO.getName());
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("[CREATE_CUSTOMER_SUCCESS] Customer saved with ID: {}", savedCustomer.getId());
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("[UPDATE_CUSTOMER] Updating customer: {}", customerDTO.getName());
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("[UPDATE_CUSTOMER_SUCCESS] Customer updated with ID: {}", savedCustomer.getId());
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        log.warn("[DELETE_CUSTOMER] Deleting customer ID: {}", customerId);
        customerRepository.deleteById(customerId);
        log.info("[DELETE_CUSTOMER_SUCCESS] Customer deleted with ID: {}", customerId);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            log.error("[CREATE_ACCOUNT_FAILED] Customer not found with ID: {}", customerId);
            throw new CustomerNotFoundException("Customer not found");
        }

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);

        log.info("[CREATE_ACCOUNT] Creating current account for customer: {}", customer.getName());
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        log.info("[CREATE_ACCOUNT_SUCCESS] Current account created with ID: {}", savedBankAccount.getId());
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            log.error("[CREATE_ACCOUNT_FAILED] Customer not found with ID: {}", customerId);
            throw new CustomerNotFoundException("Customer not found");
        }

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);

        log.info("[CREATE_ACCOUNT] Creating saving account for customer: {}", customer.getName());
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        log.info("[CREATE_ACCOUNT_SUCCESS] Saving account created with ID: {}", savedBankAccount.getId());
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        log.info("[FETCH_ACCOUNT] Fetching account by ID: {}", accountId);
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("[FETCH_ACCOUNT_FAILED] Bank account not found: {}", accountId);
                    return new BankAccountNotFoundException("Bank account not found");
                });
        log.info("[FETCH_ACCOUNT_SUCCESS] Account found with ID: {}", accountId);
        if (bankAccount instanceof SavingAccount)
            return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
        else
            return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        log.info("[DEBIT] Account: {}, Amount: {}, Description: {}", accountId, amount, description);
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("[DEBIT_FAILED] Bank account not found: {}", accountId);
                    return new BankAccountNotFoundException("Bank account not found");
                });
        if (bankAccount.getBalance() < amount) {
            log.error("[DEBIT_FAILED] Insufficient balance for account: {}, Balance: {}, Requested: {}", accountId, bankAccount.getBalance(), amount);
            throw new BalanceNotSufficientException("Insufficient balance");
        }

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

        log.info("[DEBIT_SUCCESS] Debited {} from account {}", amount, accountId);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        log.info("[CREDIT] Account: {}, Amount: {}, Description: {}", accountId, amount, description);
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("[CREDIT_FAILED] Bank account not found: {}", accountId);
                    return new BankAccountNotFoundException("Bank account not found");
                });

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);

        log.info("[CREDIT_SUCCESS] Credited {} to account {}", amount, accountId);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        log.info("[TRANSFER] From: {} → To: {}, Amount: {}", accountIdSource, accountIdDestination, amount);
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
        log.info("[TRANSFER_SUCCESS] Transfer completed from {} to {} amount {}", accountIdSource, accountIdDestination, amount);
    }

    @Override
    public AccountOperationDTO saveOperation(AccountOperationDTO accountOperationDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        log.info("[MANUAL_OPERATION] Type: {}, Account: {}, Amount: {}", accountOperationDTO.getType(), accountOperationDTO.getBankAccountDTO().getId(), accountOperationDTO.getAmount());
        BankAccount bankAccount = bankAccountRepository.findById(accountOperationDTO.getBankAccountDTO().getId())
                .orElseThrow(() -> {
                    log.error("[MANUAL_OPERATION_FAILED] Bank account not found: {}", accountOperationDTO.getBankAccountDTO().getId());
                    return new BankAccountNotFoundException("Bank account not found");
                });

        if (accountOperationDTO.getType() == OperationType.DEBIT) {
            if (bankAccount.getBalance() < accountOperationDTO.getAmount()) {
                log.error("[MANUAL_OPERATION_FAILED] Insufficient balance for account: {}, Balance: {}, Requested: {}",
                        bankAccount.getId(), bankAccount.getBalance(), accountOperationDTO.getAmount());
                throw new BalanceNotSufficientException("Insufficient balance");
            }
            bankAccount.setBalance(bankAccount.getBalance() - accountOperationDTO.getAmount());
        } else {
            bankAccount.setBalance(bankAccount.getBalance() + accountOperationDTO.getAmount());
        }

        AccountOperation accountOperation = dtoMapper.fromAccountOperationDTO(accountOperationDTO);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        AccountOperation savedAccountOperation = accountOperationRepository.save(accountOperation);

        log.info("[MANUAL_OPERATION_SUCCESS] Operation saved, ID: {}", savedAccountOperation.getId());
        return dtoMapper.fromAccountOperation(savedAccountOperation);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        log.info("[FETCH_ALL_ACCOUNTS] Found {} accounts", bankAccounts.size());
        return bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount)
                return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
            else
                return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        }).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        log.info("[FETCH_CUSTOMER] Customer ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("[FETCH_CUSTOMER_FAILED] Customer not found: {}", customerId);
                    return new CustomerNotFoundException("Customer not found");
                });
        log.info("[FETCH_CUSTOMER_SUCCESS] Customer found with ID: {}", customerId);
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        log.info("[FETCH_ALL_CUSTOMERS] Found {} customers", customers.size());
        return customers.stream().map(dtoMapper::fromCustomer).toList();
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            log.info("[SEARCH_CUSTOMERS] Empty keyword, returning all customers");
            return listCustomers();
        }
        List<Customer> customers = customerRepository.findByNameContains(keyword);
        log.info("[SEARCH_CUSTOMERS] Keyword: {}, Matches: {}", keyword, customers.size());
        return customers.stream().map(dtoMapper::fromCustomer).toList();
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        log.info("[ACCOUNT_HISTORY] Account ID: {}", accountId);
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(dtoMapper::fromAccountOperation).toList();
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        log.info("[PAGINATED_HISTORY] Account ID: {}, Page: {}, Size: {}", accountId, page, size);
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("[PAGINATED_HISTORY_FAILED] Bank account not found: {}", accountId);
                    return new BankAccountNotFoundException("Bank account not found");
                });
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(dtoMapper::fromAccountOperation).toList();
        accountHistoryDTO.setAccountOperations(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        log.info("[PAGINATED_HISTORY_SUCCESS] Account ID: {}, Returned {} operations, Total pages: {}",
                accountId, accountOperationDTOS.size(), accountOperations.getTotalPages());

        return accountHistoryDTO;
    }
}
