package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.model.Account;
import AnimalCareCentre.server.repository.AccountRepository;
import AnimalCareCentre.server.util.ACCPasswordEncryption;

@Service
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account createAccount(Account account) {
    account.setPassword(ACCPasswordEncryption.encrypt(account.getPassword()));
    return accountRepository.save(account);
  }

  public Account login(String email, String password) {
    password = ACCPasswordEncryption.encrypt(password);
    return accountRepository.findByEmailAndPassword(email, password);
  }

  public Account findAccount(String email) {
    return accountRepository.findByEmail(email);
  }

}
