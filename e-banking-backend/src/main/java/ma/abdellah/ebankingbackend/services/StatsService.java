package ma.abdellah.ebankingbackend.services;

import java.util.Map;

public interface StatsService {
    Map<String, Object> getDashboardStats(String startDate, String endDate);

    Map<String, Map<String, Double>> getOperationChartData(String startDate, String endDate);

    Map<String, Long> getAccountsByType();
}
