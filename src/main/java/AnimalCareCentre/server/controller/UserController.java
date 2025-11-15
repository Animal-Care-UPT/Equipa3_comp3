package AnimalCareCentre.server.controller;

import java.time.LocalDate;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.service.AccountService;
import AnimalCareCentre.server.service.UserService;
import AnimalCareCentre.server.util.*;

@RestController
@RequestMapping("/users/")
public class UserController {

  private final UserService userService;
  private final AccountService accountService;
  private final ACCPasswordValidator passwordValidator = new ACCPasswordValidator();
  private final EmailValidator emailValidator = EmailValidator.getInstance();

  public UserController(UserService userService, AccountService accountService) {
    this.userService = userService;
    this.accountService = accountService;
  }

  @PostMapping("/create")
  public ResponseEntity<?> createUser(@RequestBody User user) {

    if (user.getEmail() == null || user.getName() == null || user.getPassword() == null
        || user.getSecurityQuestion() == null || user.getLocation() == null || user.getAnswer() == null
        || user.getContact() == null || user.getBirthDate() == null) {
      return ResponseEntity.badRequest().body("All fields are required!");
    }

    if (!emailValidator.isValid(user.getEmail())) {
      return ResponseEntity.badRequest().body("Invalid email!");
    }

    if (user.getBirthDate().isAfter(LocalDate.now())) {
      return ResponseEntity.badRequest().body("Birthdate cannot be in the future!");
    }

    String pwError = passwordValidator.validate(user.getPassword());
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
