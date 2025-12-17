package AnimalCareCentre.server.controller;

import AnimalCareCentre.server.dto.ShelterDonationDTO;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterDonation;
import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.service.ShelterService;
import AnimalCareCentre.server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import AnimalCareCentre.server.service.ShelterDonationService;

import java.util.List;

@RestController
@RequestMapping("/donations")
public class ShelterDonationController {

  private final ShelterDonationService donationService;
  private final UserService userService;
  private final ShelterService shelterService;

  public ShelterDonationController(ShelterDonationService donationService, UserService userService,
      ShelterService shelterService) {

    this.donationService = donationService;
    this.userService = userService;
    this.shelterService = shelterService;
  }

  /**
   * To create new donations to shelters
   * 
   * @return
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/create")
  public ResponseEntity<?> newDonation(@Valid @RequestBody ShelterDonationDTO donationDTO) {

    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userService.findByEmail(email);
    if (user == null) {
      return ResponseEntity.status(404).body("User not found!");
    }

    Long shelterId = donationDTO.getShelterId();
    float donationAmount = donationDTO.getAmount();

    ShelterDonation shelterDonation = donationService.newShelterDonation(user, shelterId, donationAmount);
    if (shelterDonation == null) {
      return ResponseEntity.status(404).body("Shelter not found!");
    }
    return ResponseEntity.status(200).body(shelterDonation);
  }

  /**
   * To allow a user to see their donations made.
   * 
   * @return
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/user/historic")
  public ResponseEntity<?> listUserDonations() {

    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userService.findByEmail(email);
    if (user == null) {
      return ResponseEntity.status(404).body("User not found!");
    }

    List<ShelterDonationDTO> donations = donationService.getUserDonations(user);

    if (donations == null || donations.isEmpty()) {
      return ResponseEntity.status(404).body("No donations found!");
    }
    return ResponseEntity.status(200).body(donations);
  }

  /**
   * To allow a shelter to see their donations
   * 
   * @return
   */
  @PreAuthorize("hasRole('SHELTER')")
  @GetMapping("/shelter/donations")
  public ResponseEntity<?> listShelterDonations() {

    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Shelter shelter = shelterService.findByEmail(email);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found!");
    }

    List<ShelterDonationDTO> donations = donationService.getShelterDonations(shelter);

    if (donations == null || donations.isEmpty()) {
      return ResponseEntity.status(404).body("No donations found!");
    }

    return ResponseEntity.status(200).body(donations);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("admin/{shelterId}")
  public ResponseEntity<?> getShelterDonations(@PathVariable Long shelterId) {
    Shelter shelter = shelterService.findById(shelterId);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found");
    }

    List<ShelterDonationDTO> donations = donationService.getShelterDonations(shelter);

    if (donations.isEmpty()) {
      return ResponseEntity.status(404).body("No donations found for this shelter");
    }

    return ResponseEntity.status(200).body(donations);
  }

}
