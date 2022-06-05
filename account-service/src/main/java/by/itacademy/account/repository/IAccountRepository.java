package by.itacademy.account.repository;

import by.itacademy.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<Account, String> {

    Account findByUuid(String uuid);

}
