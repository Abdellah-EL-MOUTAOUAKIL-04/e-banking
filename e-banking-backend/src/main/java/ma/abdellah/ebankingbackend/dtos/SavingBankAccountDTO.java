package ma.abdellah.ebankingbackend.dtos;

import lombok.*;
import ma.abdellah.ebankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Data
public class SavingBankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
