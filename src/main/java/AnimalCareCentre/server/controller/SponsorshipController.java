package AnimalCareCentre.server.controller;

import java.util.List;

import AnimalCareCentre.server.repository.SponsorshipRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import AnimalCareCentre.server.dto.*;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.model.Sponsorship;
import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.service.ShelterAnimalService;
import AnimalCareCentre.server.service.SponsorshipService;
import AnimalCareCentre.server.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sponsorships")
public class SponsorshipController {

  private final SponsorshipService sponsorshipService;
  private final UserService userService;
  private final ShelterAnimalService shelterAnimalService;
    private final SponsorshipRepository sponsorshipRepository;

    public SponsorshipController(SponsorshipService sponsorshipService, UserService userService,
                               ShelterAnimalService shelterAnimalService, SponsorshipRepository sponsorshipRepository) {
    this.sponsorshipService = sponsorshipService;
    this.userService = userService;
    this.shelterAnimalService = shelterAnimalService;
        this.sponsorshipRepository = sponsorshipRepository;
    }

    /**
     * To create a new sponsorship
     * @param animalId
     * @param amount
     * @return
     */
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/create")
  public ResponseEntity<?> createNewSponsorship (@Valid @RequestParam Long animalId, @RequestParam float amount){
      String email = SecurityContextHolder.getContext().getAuthentication().getName();
      User user = userService.findByEmail(email);

      if (user == null) {
          return ResponseEntity.status(404).body("User not found!");
      }

      ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(animalId);
      if (animal == null) {
          return ResponseEntity.status(404).body("Animal not found!");
      }

      Sponsorship sponsorship = sponsorshipService.newSponsorship(user, animal, amount);
      return ResponseEntity.status(200).body(sponsorship);

  }

    /**
     * To cancel a sponsorship
     * @param sponsorshipId
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelSponsorship (@RequestParam Long sponsorshipId){
        Sponsorship sponsorship = sponsorshipRepository.findById(sponsorshipId).orElse(null);

        if (sponsorship == null) {
            return ResponseEntity.status(404).body("Sponsorship not found!");
        }

        sponsorshipService.cancelSponsorship(sponsorshipId);
        return ResponseEntity.ok("Sponsorship cancelled successfully!");
    }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/user/historic")
  public ResponseEntity<?> listUserSponsorships(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found!");
        }

        List<SponsorshipDTO> sponsorships = sponsorshipService.getUserSponsorshipDTOs(user);
        return ResponseEntity.status(200).body(sponsorships);
  }

    /**
     * So a shelter can see the sponsorships of a certain animal
     * @param animalId
     * @return
     */
  @PreAuthorize("hasAnyRole('SHELTER','ADMIN', 'USER')")
  @GetMapping("/animal/{animalId}")
  public ResponseEntity<?> listShelterSponsorships(@PathVariable Long animalId){
      ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(animalId);
      if (animal == null) {
          return ResponseEntity.status(404).body("Animal not found!");
      }

      List<Sponsorship> sponsorships = sponsorshipService.getAnimalSponsorships(animal);
      List<SponsorshipDTO> dtos = sponsorships.stream().map(SponsorshipDTO::fromEntity).toList();

      return ResponseEntity.ok(dtos);

  }

    /**
     * To allow and Admin to see all the sponsorships
     * @return
     */
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin")
  public ResponseEntity<?> listAllSponsorships(){
      List<Sponsorship> sponsorships = sponsorshipService.getAllSponsorships();

      if (sponsorships.isEmpty()) {
          return ResponseEntity.status(404).body("No sponsorships found");
      }

      return ResponseEntity.status(200).body(sponsorships);
  }

}
