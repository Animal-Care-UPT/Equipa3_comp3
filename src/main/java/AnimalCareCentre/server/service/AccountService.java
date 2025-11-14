package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.model.Account;
import AnimalCareCentre.server.repository.AccountRepository;

@Service
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account createAccount(Account account) {
    return accountRepository.save(account);
  }

}
