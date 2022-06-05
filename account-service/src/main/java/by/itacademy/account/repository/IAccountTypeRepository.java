package by.itacademy.account.repository;

import by.itacademy.account.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountTypeRepository extends JpaRepository<AccountType, String> {
}
