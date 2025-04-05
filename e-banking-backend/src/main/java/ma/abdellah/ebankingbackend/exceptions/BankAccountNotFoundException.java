package ma.abdellah.ebankingbackend.exceptions;

public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException(String bankAccountNotFound) {
        super(bankAccountNotFound);
    }
}
