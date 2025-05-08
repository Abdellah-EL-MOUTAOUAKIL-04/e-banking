package ma.abdellah.ebankingbackend.web;

import lombok.AllArgsConstructor;
import ma.abdellah.ebankingbackend.services.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/stats")
@AllArgsConstructor
public class StatsRestController {
    private StatsService statsService;

    @GetMapping("/operations")
    public Map<String, Object> getOperationsStats(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return statsService.getDashboardStats(startDate, endDate);
    }

    @GetMapping("/operations-chart")
    public Map<String, Map<String, Double>> getOperationsChartData(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return statsService.getOperationChartData(startDate, endDate);
    }
}
