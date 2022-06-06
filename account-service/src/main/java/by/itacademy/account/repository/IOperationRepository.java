package by.itacademy.account.repository;

import by.itacademy.account.entity.Account;
import by.itacademy.account.entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IOperationRepository extends PagingAndSortingRepository<Operation, String> {

    Operation findByUuid(String uuid);

    @Query(value = "SELECT * FROM finance_app.operation o WHERE o.account_id = ?1",
            nativeQuery = true)
    List<Operation> findAllOperationByAccountId(String accountId);

    Page<Operation> findAllByAccountUuid(Account accountUid, Pageable pageable);
}
