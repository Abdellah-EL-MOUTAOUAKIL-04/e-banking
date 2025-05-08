package ma.abdellah.ebankingbackend.services;

import lombok.AllArgsConstructor;
import ma.abdellah.ebankingbackend.entities.AccountOperation;
import ma.abdellah.ebankingbackend.enums.OperationType;
import ma.abdellah.ebankingbackend.repositories.AccountOperationRepository;
import ma.abdellah.ebankingbackend.repositories.BankAccountRepository;
import ma.abdellah.ebankingbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;

    @Override
    public Map<String, Object> getDashboardStats(String startDate, String endDate) {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Parse start and end date strings to Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            // Fetch operations based on the date range
            List<AccountOperation> operations = accountOperationRepository.findByOperationDateBetween(start, end);

            // Calculate the total number of operations and the total amount
            int totalOperations = operations.size();
            double totalAmount = operations.stream().mapToDouble(AccountOperation::getAmount).sum();

            long numberOfAccounts = bankAccountRepository.count();
            long numberOfCustomers = customerRepository.count();

            // Add statistics to the map
            stats.put("numberOfAccounts", numberOfAccounts);
            stats.put("numberOfCustomers", numberOfCustomers);
            stats.put("totalOperations", totalOperations);
            stats.put("totalAmount", totalAmount);

        } catch (Exception e) {
            stats.put("error", "Invalid date format");
        }

        return stats;
    }

    @Override
    public Map<String, Map<String, Double>> getOperationChartData(String startDate, String endDate) {
        Map<String, Map<String, Double>> chartData = new HashMap<>();
        Map<String, Double> debitData = new HashMap<>();
        Map<String, Double> creditData = new HashMap<>();
        Map<String, Double> transferData = new HashMap<>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            List<AccountOperation> operations = accountOperationRepository.findByOperationDateBetween(start, end);
            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

            for (AccountOperation operation : operations) {
                String day = dayFormat.format(operation.getOperationDate());
                double amount = operation.getAmount();
                OperationType type = operation.getType();

                if (type == OperationType.DEBIT) {
                    debitData.put(day, debitData.getOrDefault(day, 0.0) + amount);
                } else if (type == OperationType.CREDIT) {
                    creditData.put(day, creditData.getOrDefault(day, 0.0) + amount);
                }
            }

            chartData.put("debit", debitData);
            chartData.put("credit", creditData);
            chartData.put("transfer", transferData);

        } catch (Exception e) {
            // Optional: Add error logging
        }

        return chartData;
    }
}
