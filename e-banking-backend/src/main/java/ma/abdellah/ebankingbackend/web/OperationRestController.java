package ma.abdellah.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.abdellah.ebankingbackend.dtos.AccountOperationDTO;
import ma.abdellah.ebankingbackend.exceptions.BalanceNotSufficientException;
import ma.abdellah.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.abdellah.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class OperationRestController {
    private BankAccountService bankAccountService;
    //new operation
    @PostMapping("/operations")
    public AccountOperationDTO saveOperation(@RequestBody AccountOperationDTO accountOperationDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        //check if the account exists
        // get auth user
        System.out.println("accountOperationDTO ==>>>>>>>>>>>>>>>> " + accountOperationDTO);
        return bankAccountService.saveOperation(accountOperationDTO);
    }
    //delete operation

}
