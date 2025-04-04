package ma.abdellah.ebankingbackend.repositories;

import ma.abdellah.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccounRepository extends JpaRepository<BankAccount,String> {
}
