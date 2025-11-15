package AnimalCareCentre.server.controller;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.enums.Status;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.service.AccountService;
import AnimalCareCentre.server.service.ShelterService;
import AnimalCareCentre.server.util.*;

@RestController
@RequestMapping("/shelters/")
public class ShelterController {

  private final ShelterService shelterService;
  private final AccountService accountService;
  private final ACCPasswordValidator passwordValidator = new ACCPasswordValidator();
  private final EmailValidator emailValidator = EmailValidator.getInstance();

  public ShelterController(ShelterService shelterService, AccountService accountService) {
    this.shelterService = shelterService;
    this.accountService = accountService;
  }

  @PostMapping("/create")
  public ResponseEntity<?> createUser(@RequestBody Shelter shelter) {

    if (shelter.getEmail() == null || shelter.getName() == null || shelter.getPassword() == null
        || shelter.getSecurityQuestion() == null || shelter.getLocation() == null || shelter.getAnswer() == null
        || shelter.getContact() == null || shelter.getFoundationYear() == null) {
      return ResponseEntity.badRequest().body("All fields are required!");
    }

    if (!emailValidator.isValid(shelter.getEmail())) {
      return ResponseEntity.badRequest().body("Invalid email!");
    }

    if (shelter.getFoundationYear() > LocalDate.now().getYear()) {
      return ResponseEntity.badRequest().body("Foundation year cannot be in the future!");
    }

    String pwError = passwordValidator.validate(shelter.getPassword());
    if (pwError != null) {
      return ResponseEntity.badRequest().body(pwError);
    }

    if (accountService.findAccount(shelter.getEmail()) != null) {
      return ResponseEntity.status(409).body("Email already registered!");
    }
    Shelter s = shelterService.createShelter(shelter);
    s.setPassword(null);
    return ResponseEntity.status(201).body(s);
  }

  @PutMapping("/status")
  public Shelter changeStatus(@RequestBody Shelter shelter, @RequestParam Status status) {
    return shelterService.changeStatus(shelter, status);
  }

  @GetMapping("/pending")
  public ResponseEntity<?> getPendingShelters() {
    List<Shelter> shelters = shelterService.getPendingShelters();
    if (shelters.isEmpty()) {
      return ResponseEntity.status(404).body("There are no pending Shelters!");
    }
      return ResponseEntity.ok(shelters);
  }

  @GetMapping
  public ResponseEntity<?> viewShelters() {
    List<Shelter> shelters = shelterService.getShelters();
    if (shelters.isEmpty()) {
      return ResponseEntity.status(404).body("There are no registered shelters!");
    }
    return ResponseEntity.ok(shelters);
  }
}
