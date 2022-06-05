package by.itacademy.account.repository;

import by.itacademy.account.entity.Account;
import by.itacademy.account.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBalanceRepository extends JpaRepository<Balance, Account> {

}
