package ma.abdellah.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int CurrentPage;
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO> accountOperations;
}
