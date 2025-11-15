package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.enums.*;
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

  public Account changePassword(String email, String password) {
    Account acc = accountRepository.findByEmail(email);
    if (acc == null) {
      return null;
    }
    password = ACCPasswordEncryption.encrypt(password);
    acc.setPassword(password);
    return accountRepository.save(acc);
  }

  public SecurityQuestion getSecurityQuestion(String email) {
    Account acc = findAccount(email);
    if (acc != null) {
      return acc.getSecurityQuestion();
    }
    return null;
  }

  public boolean verifySecurityAnswer(String email, String answer) {
    Account acc = findAccount(email);
    if (acc != null) {
      if (acc.getAnswer().equals(answer)) {
        return true;
      }
    }
    return false;
  }

}
