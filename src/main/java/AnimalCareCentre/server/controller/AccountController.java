package AnimalCareCentre.server.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.model.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import AnimalCareCentre.server.service.AccountService;

@RestController
@RequestMapping("/accounts/")
public class AccountController {

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PutMapping("/changepw")
  public ResponseEntity<?> changePassword(@Email @NotBlank @RequestParam String email, @NotBlank @RequestParam String newPW, @NotBlank @RequestParam String answer) {

    if (accountService.findAccount(email) == null) {
      return ResponseEntity.status(404).body("Account not registered!");
    }

    if (!accountService.verifySecurityAnswer(email, answer)) {
      return ResponseEntity.status(403).body("Invalid answer!");
    }

    String pwError = accountService.verifyPasswordRules(newPW);
    if (pwError != null) {
      return ResponseEntity.badRequest().body(pwError);
    }

    accountService.changePassword(email, newPW);

    return ResponseEntity.ok("Password changed successfully");
  }

  @PostMapping("/create")
  public ResponseEntity<?> createAccount(@Valid @RequestBody Account account, @RequestParam String secret) {

    if (!accountService.verifyAdminSecret(secret)) {
      return ResponseEntity.status(403).body("Invalid admin secret word!");

    }
    String pwError = accountService.verifyPasswordRules(account.getPassword());
    if (pwError != null) {
      return ResponseEntity.status(400).body(pwError);
    }

    if (accountService.findAccount(account.getEmail()) != null) {
      return ResponseEntity.status(409).body("Email already registered!");
    }

    Account acc = accountService.createAccount(account);
    acc.setPassword(null);
    return ResponseEntity.status(201).body(acc);
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

  @PostMapping("/secquestion")
  public ResponseEntity<?> getSecurityQuestion(@RequestParam String email) {
    Account acc = accountService.findAccount(email);
    if (acc == null) {
      return ResponseEntity.status(404).body("Email not registered!");
    }
    return ResponseEntity.ok(acc.getSecurityQuestion());
  }
}
