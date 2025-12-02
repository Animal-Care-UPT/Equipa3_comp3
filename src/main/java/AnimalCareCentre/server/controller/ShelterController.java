package AnimalCareCentre.server.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.dto.ChangeStatusDTO;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.service.AccountService;
import AnimalCareCentre.server.service.ShelterService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shelters/")
public class ShelterController {

  private final ShelterService shelterService;
  private final AccountService accountService;

  public ShelterController(ShelterService shelterService, AccountService accountService) {
    this.shelterService = shelterService;
    this.accountService = accountService;
  }

  @PostMapping("/create")
  public ResponseEntity<?> createShelter(@Valid @RequestBody Shelter shelter) {

    if (shelter.getFoundationYear() > LocalDate.now().getYear()) {
      return ResponseEntity.badRequest().body("Foundation year cannot be in the future!");
    }

    String pwError = accountService.verifyPasswordRules(shelter.getPassword());
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

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/status")
  public ResponseEntity<?> changeStatus(@RequestParam long id, @Valid @RequestBody ChangeStatusDTO status) {
    Shelter shelter = shelterService.findById(id);
    if (shelter != null) {
      shelterService.changeStatus(shelter, status.getStatus());
      return ResponseEntity.ok().body("Changed shelter status successfully");
    }
    return ResponseEntity.status(404).body("Shelter not found!");
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/pending")
  public ResponseEntity<?> getPendingShelters() {
    List<Shelter> shelters = shelterService.getPendingShelters();
    if (shelters.isEmpty()) {
      return ResponseEntity.status(404).body("There are no pending Shelters!");
    }
    return ResponseEntity.ok(shelters);
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public ResponseEntity<?> viewAvailableShelters() {
    List<Shelter> shelters = shelterService.getShelters();
    if (shelters.isEmpty()) {
      return ResponseEntity.status(404).body("There are no registered shelters!");
    }
    return ResponseEntity.ok(shelters);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  public ResponseEntity<?> viewAllShelters() {
    List<Shelter> shelters = shelterService.getAllShelters();
    if (shelters.isEmpty()) {
      return ResponseEntity.status(404).body("There are no registered shelters!");
    }
    return ResponseEntity.ok(shelters);
  }
}
