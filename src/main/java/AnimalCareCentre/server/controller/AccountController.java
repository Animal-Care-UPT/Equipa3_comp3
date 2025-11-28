package AnimalCareCentre.server.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.dto.LoginRequest;
import AnimalCareCentre.server.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import AnimalCareCentre.server.service.AccountService;

@RestController
@RequestMapping("/accounts/")
public class AccountController {

  private final AccountService accountService;
  private final AuthenticationManager authenticationManager;

  public AccountController(AccountService accountService, AuthenticationManager authenticationManager) {
    this.accountService = accountService;
    this.authenticationManager = authenticationManager;
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

  @PutMapping("/changepw")
  public ResponseEntity<?> changePassword(@Email @NotBlank @RequestParam String email,
      @NotBlank @RequestParam String newPW, @NotBlank @RequestParam String answer) {

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

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
    try {

      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(),
          request.getPassword());

      Authentication auth = authenticationManager.authenticate(token);

      SecurityContext securityContext = SecurityContextHolder.getContext();
      securityContext.setAuthentication(auth);
      HttpSession session = httpRequest.getSession(true);
      session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

      Account account = accountService.findAccount(request.getEmail());
      account.setPassword(null);

      if (account instanceof Shelter) {
        Shelter shelter = (Shelter) account;
        return ResponseEntity.ok(Map.of("type", "SHELTER", "account", (Shelter) shelter));

      } else if (account instanceof User) {
        User user = (User) account;
        return ResponseEntity.ok(Map.of("type", "USER", "account", (User) user));

      } else {
        return ResponseEntity.ok(Map.of("type", "ADMIN", "account", account));
      }

    } catch (AuthenticationException e) {

      return ResponseEntity.status(401).body("Invalid email or password!");
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

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    SecurityContextHolder.clearContext();
    return ResponseEntity.ok("Logged out successfully");
  }
}
