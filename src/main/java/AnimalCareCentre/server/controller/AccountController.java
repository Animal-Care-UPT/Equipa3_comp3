package AnimalCareCentre.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.dto.AdminCreateDTO;
import AnimalCareCentre.server.dto.ChangePasswordDTO;
import AnimalCareCentre.server.dto.LoginRequestDTO;
import AnimalCareCentre.server.dto.SecurityQuestionDTO;
import AnimalCareCentre.server.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
  public ResponseEntity<?> createAccount(@Valid @RequestBody AdminCreateDTO acc) {

    if (!accountService.verifyAdminSecret(acc.getSecret())) {
      return ResponseEntity.status(403).body("Invalid admin secret word!");
    }

    String pwError = accountService.verifyPasswordRules(acc.getPassword());
    if (pwError != null) {
      return ResponseEntity.status(400).body(pwError);
    }

    if (accountService.findAccount(acc.getEmail()) != null) {
      return ResponseEntity.status(409).body("Email already registered!");
    }

    Account account = accountService.createAccount(acc.getName(), acc.getEmail(), acc.getPassword(), acc.getLocation(),
        acc.getSecurityQuestion(), acc.getAnswer());
    account.setPassword(null);
    return ResponseEntity.status(201).body(account);
  }

  @PutMapping("/changepw")
  public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO pwRequest) {

    if (accountService.findAccount(pwRequest.getEmail()) == null) {
      return ResponseEntity.status(404).body("Account not registered!");
    }

    if (!accountService.verifySecurityAnswer(pwRequest.getEmail(), pwRequest.getAnswer())) {
      return ResponseEntity.status(400).body("Invalid answer!");
    }

    String pwError = accountService.verifyPasswordRules(pwRequest.getNewPassword());
    if (pwError != null) {
      return ResponseEntity.badRequest().body(pwError);
    }

    accountService.changePassword(pwRequest.getEmail(), pwRequest.getNewPassword());

    return ResponseEntity.ok("Password changed successfully");
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request, HttpServletRequest httpRequest) {
    try {

      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(),
          request.getPassword());

      Authentication auth = authenticationManager.authenticate(token);

      SecurityContext securityContext = SecurityContextHolder.getContext();
      securityContext.setAuthentication(auth);
      HttpSession session = httpRequest.getSession(true);
      session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

      String role = securityContext.getAuthentication().getAuthorities().iterator().next().getAuthority();

      return ResponseEntity.ok(role);

    } catch (AuthenticationException e) {

      return ResponseEntity.status(400).body("Invalid email or password!");
    }
  }

  @GetMapping("/secquestion")
  public ResponseEntity<?> getSecurityQuestion(@NotBlank @RequestParam String email) {
    Account acc = accountService.findAccount(email);
    if (acc == null) {
      return ResponseEntity.status(404).body("Email not registered!");
    }
    return ResponseEntity.ok(acc.getSecurityQuestion().toString());
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

  @PutMapping("/changesq")
  public ResponseEntity<?> changeSecurityQuestion(@Valid @RequestBody SecurityQuestionDTO requestSecurity){
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    if(accountService.findAccount(email)==null){
      return ResponseEntity.status(404).body("User not found");
    }
    accountService.changeSQandAns(email, requestSecurity.getSecurityQuestion(), requestSecurity.getAnswer());
    return ResponseEntity.ok("Security info changed successfully");
  }
}
