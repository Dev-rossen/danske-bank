package dk.rossen.dbdemo.repository;

import dk.rossen.dbdemo.repository.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
    Optional<List<AccountEntity>> findAllByCustomerId(String customerId);

    @Modifying
    @Query(value = "UPDATE accounts SET balance = :balance WHERE account_no = :debtorAccountNumber", nativeQuery = true)
    void updateBalance(@Param("debtorAccountNumber") String accountNumber, @Param("balance") float balance);
}
