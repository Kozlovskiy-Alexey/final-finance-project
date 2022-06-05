package by.itacademy.account.repository;

import by.itacademy.account.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOperationRepository extends JpaRepository<Operation, String> {

    Operation findByUuid(String uuid);

    @Query(value = "SELECT * FROM finance_app.operation o WHERE o.account_id = ?1",
            nativeQuery = true)
    List<Operation> findAllOperationByAccountId(String accountId);
}
