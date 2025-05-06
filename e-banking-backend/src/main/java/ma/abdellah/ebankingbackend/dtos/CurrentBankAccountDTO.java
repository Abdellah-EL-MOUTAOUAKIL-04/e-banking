package ma.abdellah.ebankingbackend.dtos;

import lombok.Data;
import ma.abdellah.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO {
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
}
