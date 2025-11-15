package AnimalCareCentre.server.controller;

import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.model.*;
import AnimalCareCentre.server.util.*;
import AnimalCareCentre.server.service.AccountService;

@RestController
@RequestMapping("/accounts/")
public class AccountController {

  @Value("${admin.secret.word}")
  private String adminSecretWord;
  private final AccountService accountService;
  private final ACCPasswordValidator passwordValidator = new ACCPasswordValidator();
  private final EmailValidator emailValidator = EmailValidator.getInstance();

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/create")
  public ResponseEntity<?> createAccount(@RequestBody Account account, @RequestParam String secret) {

    if (!secret.equals(adminSecretWord)) {
      return ResponseEntity.status(403).body("Invalid admin secret word!");
    }

    if (account.getEmail() == null || account.getName() == null || account.getPassword() == null
        || account.getSecurityQuestion() == null || account.getLocation() == null || account.getAnswer() == null) {
      return ResponseEntity.badRequest().body("All fields are required!");
    }

    if (!emailValidator.isValid(account.getEmail())) {
      return ResponseEntity.badRequest().body("Invalid email!");
    }

    String pwError = passwordValidator.validate(account.getPassword());
    if (pwError != null) {
      return ResponseEntity.badRequest().body(pwError);
    }

    if (accountService.findAccount(account.getEmail()) != null) {
      return ResponseEntity.status(409).body("Email already registered!");
    }
    Account acc = accountService.createAccount(account);
    acc.setPassword(null);
    return ResponseEntity.status(201).body(acc);
  }

  @GetMapping("/search")
  public Account searchAccount(@RequestParam String email) {
    return accountService.findAccount(email);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {

    Account account = accountService.login(email, password);

    if (account == null) {
      return ResponseEntity.status(401).body("Invalid email or password!");
    }

    if (account instanceof Shelter) {
      Shelter shelter = (Shelter) account;
      shelter.setPassword(null);
      return ResponseEntity.ok(Map.of("type", "SHELTER", "account", shelter));

    } else if (account instanceof User) {
      User user = (User) account;
      user.setPassword(null);
      return ResponseEntity.ok(Map.of("type", "USER", "account", user));

    } else {
      account.setPassword(null);
      return ResponseEntity.ok(Map.of("type", "ADMIN", "account", account));
    }
  }
}
