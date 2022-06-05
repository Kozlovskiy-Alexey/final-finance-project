package by.itacademy.account.scheduler.repository;

import by.itacademy.account.scheduler.repository.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOperationRepository extends JpaRepository<Operation, String> {
}
