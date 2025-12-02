package AnimalCareCentre.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.service.AccountService;
import AnimalCareCentre.server.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/")
public class UserController {

  private final UserService userService;
  private final AccountService accountService;

  public UserController(UserService userService, AccountService accountService) {
    this.userService = userService;
    this.accountService = accountService;
  }

  @PostMapping("/create")
  public ResponseEntity<?> createUser(@Valid @RequestBody User user) {

    String pwError = accountService.verifyPasswordRules(user.getPassword());
    if (pwError != null) {
      return ResponseEntity.badRequest().body(pwError);
    }

    if (accountService.findAccount(user.getEmail()) != null) {
      return ResponseEntity.status(409).body("Email already registered!");
    }
    User u = userService.createUser(user);
    u.setPassword(null);
    return ResponseEntity.status(201).body(u);
  }

}
