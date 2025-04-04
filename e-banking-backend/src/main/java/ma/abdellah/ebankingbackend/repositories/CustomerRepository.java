package ma.abdellah.ebankingbackend.repositories;

import ma.abdellah.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
