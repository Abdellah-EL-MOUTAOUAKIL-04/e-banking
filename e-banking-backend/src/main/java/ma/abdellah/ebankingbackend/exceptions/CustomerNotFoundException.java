package ma.abdellah.ebankingbackend.exceptions;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String customerNotFound) {
        super(customerNotFound);
    }
}
