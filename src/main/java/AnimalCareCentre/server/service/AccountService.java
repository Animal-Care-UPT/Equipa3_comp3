package AnimalCareCentre.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.enums.SecurityQuestion;
import AnimalCareCentre.server.model.Account;
import AnimalCareCentre.server.repository.AccountRepository;
import AnimalCareCentre.server.util.*;

@Service
public class AccountService {

  @Value("${admin.secret.word}")
  private String adminSecretWord;
  private final AccountRepository accountRepository;
  private final ACCPasswordValidator passwordValidator = new ACCPasswordValidator();
  @Autowired
  private final PasswordEncoder passwordEncoder;

  public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Account createAccount(String name, String email, String password, String location, SecurityQuestion securityQuestion, String answer) {

    Account acc = new Account();
    acc.setName(name);
    acc.setEmail(email);
    acc.setPassword(passwordEncoder.encode(password));
    acc.setLocation(location);
    acc.setSecurityQuestion(securityQuestion);
    acc.setAnswer(answer);
    return accountRepository.save(acc);
  }

  public Account findAccount(String email) {
    return accountRepository.findByEmail(email);
  }

  public Account changePassword(String email, String password) {
    Account acc = accountRepository.findByEmail(email);
    if (acc == null) {
      return null;
    }
    acc.setPassword(passwordEncoder.encode(password));
    return accountRepository.save(acc);
  }

  public Account changeSQandAns(String email, SecurityQuestion question, String answer){
      Account acc = accountRepository.findByEmail(email);
      if (acc == null) {
        return null;
      }
      if (question != null && !question.toString().isBlank()) {
        acc.setSecurityQuestion(question);
      }
      if (answer != null && !answer.isBlank()) {
        acc.setAnswer(answer);
      }
      return accountRepository.save(acc);
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

  public boolean verifyAdminSecret(String secret) {
    if (secret.equals(adminSecretWord)) {
      return true;
    }
    return false;
  }

  public String verifyPasswordRules(String password) {
    String pwError = passwordValidator.validate(password);
    return pwError;

  }

}
