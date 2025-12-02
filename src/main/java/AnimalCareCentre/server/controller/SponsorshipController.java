package AnimalCareCentre.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.dto.*;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.model.Sponsorship;
import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.service.ShelterAnimalService;
import AnimalCareCentre.server.service.SponsorshipService;
import AnimalCareCentre.server.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sponsorships/")
public class SponsorshipController {

  private final SponsorshipService sponsorshipService;
  private final UserService userService;
  private final ShelterAnimalService shelterAnimalService;

  public SponsorshipController(SponsorshipService sponsorshipService, UserService userService,
      ShelterAnimalService shelterAnimalService) {
    this.sponsorshipService = sponsorshipService;
    this.userService = userService;
    this.shelterAnimalService = shelterAnimalService;
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping("/create")
  public ResponseEntity<?> createSponsorShip(@RequestBody SponsorshipRequestDTO sponsorship) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userService.findByEmail(email);
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(sponsorship.getAnimalId());

    if (animal == null) {
      return ResponseEntity.status(404).body("Animal not found");
    }

    if (animal.getSponsors().size() >= 3) {
      return ResponseEntity.status(409).body("The animal already has 3 sponsors");
    }

    sponsorshipService.createSponsorShip(user, animal, sponsorship.getAmount());
    return ResponseEntity.status(201).body("You sponsored the animal with success!");
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/usersponsor")
  public ResponseEntity<?> getUserSponsorShips() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userService.findByEmail(email);
    List<SponsorshipDTO> results = sponsorshipService.searchSponsorshipsUser(user);
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no sponsoships registered");
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  public ResponseEntity<?> searchAllSponsorships() {
    List<SponsorshipDTO> results = sponsorshipService.searchAll();
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no SponsorShips registered");
  }
}
