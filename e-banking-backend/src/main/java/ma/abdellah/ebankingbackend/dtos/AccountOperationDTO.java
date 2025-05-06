package ma.abdellah.ebankingbackend.dtos;

import lombok.Data;
import ma.abdellah.ebankingbackend.enums.OperationType;

import java.util.Date;
@Data
public class AccountOperationDTO {
    private Long id;
    private BankAccountDTO bankAccountDTO;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
