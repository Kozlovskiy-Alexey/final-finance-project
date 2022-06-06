package by.itacademy.account.repository;

import by.itacademy.account.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IAccountRepository extends PagingAndSortingRepository<Account, String> {

    Account findByUuid(String uuid);

    Page<Account> findAll(Pageable pageable);

}
