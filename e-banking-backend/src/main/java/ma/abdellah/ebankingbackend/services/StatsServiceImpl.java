package ma.abdellah.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StatsServiceImpl implements StatsService {
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;

    @Override
    public Map<String, Object> getDashboardStats(String startDate, String endDate) {
        log.info("[GET_DASHBOARD_STATS] startDate={}, endDate={}", startDate, endDate);
        Map<String, Object> stats = new HashMap<>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            List<AccountOperation> operations = accountOperationRepository.findByOperationDateBetween(start, end);
            int totalOperations = operations.size();
            double totalAmount = operations.stream().mapToDouble(AccountOperation::getAmount).sum();

            long numberOfAccounts = bankAccountRepository.count();
            long numberOfCustomers = customerRepository.count();

            stats.put("numberOfAccounts", numberOfAccounts);
            stats.put("numberOfCustomers", numberOfCustomers);
            stats.put("totalOperations", totalOperations);
            stats.put("totalAmount", totalAmount);

            log.info("[STATS_COMPUTED] accounts={}, customers={}, operations={}, amount={}",
                    numberOfAccounts, numberOfCustomers, totalOperations, totalAmount);
        } catch (Exception e) {
            log.error("[GET_DASHBOARD_STATS_ERROR] Invalid date format or error", e);
            stats.put("error", "Invalid date format");
        }

        return stats;
    }

    @Override
    public Map<String, Map<String, Double>> getOperationChartData(String startDate, String endDate) {
        log.info("[GET_OPERATION_CHART_DATA] startDate={}, endDate={}", startDate, endDate);
        Map<String, Map<String, Double>> chartData = new HashMap<>();
        Map<String, Double> debitData = new HashMap<>();
        Map<String, Double> creditData = new HashMap<>();
        Map<String, Double> transferData = new HashMap<>();  // Peut être rempli si tu ajoutes des transferts

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            Calendar cal = Calendar.getInstance();
            cal.setTime(end);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            end = cal.getTime();

            List<AccountOperation> operations = accountOperationRepository.findByOperationDateBetween(start, end);
            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

            log.info("[OPERATIONS_FOUND] Count={}", operations.size());

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

            log.info("[CHART_DATA_PREPARED] debitDays={}, creditDays={}", debitData.size(), creditData.size());
        } catch (Exception e) {
            log.error("[GET_OPERATION_CHART_DATA_ERROR]", e);
        }

        return chartData;
    }

    @Override
    public Map<String, Long> getAccountsByType() {
        log.info("[GET_ACCOUNTS_BY_TYPE] Start counting accounts by type");
        Map<String, Long> typeStats = new HashMap<>();
        bankAccountRepository.findAll().forEach(account -> {
            String type = account.getClass().getSimpleName();
            typeStats.put(type, typeStats.getOrDefault(type, 0L) + 1);
        });
        log.info("[ACCOUNTS_BY_TYPE_RESULT] {}", typeStats);
        return typeStats;
    }
}
