package ma.abdellah.ebankingbackend.exceptions;

public class BalanceNotSufficientException extends Exception {
    public BalanceNotSufficientException(String insufficientBalance) {
        super(insufficientBalance);
    }
}
