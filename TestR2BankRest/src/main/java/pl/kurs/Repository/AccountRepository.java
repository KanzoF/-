package pl.kurs.Repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Account, Long> {


    //    @Query("SELECT a FROM Account a WHERE a.id IN :accountIds ORDER BY a.id")
//    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
//@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="5000")})

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :accountId")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
    Optional<Account> findByIdWithLock(@Param("accountId") Long accountId);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("SELECT a FROM Account a WHERE a.id IN :accountIds")
//    List<Account> findByIdsWithLock(Set<Long> accountIds);


//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("SELECT a FROM Account a WHERE a.id = :accountId")
//    Account findByIdWithLock(@Param("accountId") Long accountId);


    //    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.id = :accountId")
    void updateAccountBalance(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);

    @EntityGraph(attributePaths = {"outgoingTransactions", "incomingTransactions"})
    @Query("select a from Account a")
    List<Account> findAllWithTransactions();
}
