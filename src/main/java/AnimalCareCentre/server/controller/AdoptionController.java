package AnimalCareCentre.server.controller;

import AnimalCareCentre.server.dto.*;
import AnimalCareCentre.server.enums.AdoptionType;
import AnimalCareCentre.server.model.*;
import AnimalCareCentre.server.enums.Status;
import AnimalCareCentre.server.service.AdoptionService;
import AnimalCareCentre.server.service.ShelterAnimalService;
import AnimalCareCentre.server.service.ShelterService;
import AnimalCareCentre.server.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adoptions/")
public class AdoptionController {

  private final AdoptionService adoptionService;
  private final UserService userService;
  private final ShelterService shelterService;
  private final ShelterAnimalService shelterAnimalService;

  public AdoptionController(AdoptionService adoptionService, ShelterAnimalService shelterAnimalService,
      UserService userService, ShelterService shelterService) {

    this.adoptionService = adoptionService;
    this.userService = userService;
    this.shelterService = shelterService;
    this.shelterAnimalService = shelterAnimalService;
  }

    /**
     * Adoption request
     * @param dto
     * @return
     */
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/request")
  public ResponseEntity<?> requestAdoption(@RequestParam Long animalId, @RequestParam String type) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userService.findByEmail(email);

    if (user == null) {
      return ResponseEntity.status(404).body("This user isn't registered!");
    }

    String enumValue = switch (type.toUpperCase()) {
        case "ADOPTION" -> "FOR_ADOPTION";
        case "FOSTER" -> "FOR_FOSTER";
        default -> null;
      };

      if (enumValue == null) {
          return ResponseEntity.status(400).body("Invalid adoption type.");
      }

      AdoptionType adoptionType = AdoptionType.fromString(enumValue);

    Adoption adoption = adoptionService.requestAdoption(user, animalId, adoptionType);
    return ResponseEntity.status(201).body(adoption);
  }


    /**
     * To change the status of an adoption/foster request
     * @param status
     * @return
     */
  @PreAuthorize("hasRole('SHELTER')")
  @PutMapping("/change/status")
  public ResponseEntity<?> changeStatus(@RequestBody AdoptionChangeStatusDTO status) {

    Adoption adoption = adoptionService.findAdoptionById(status.getAdoptionId());
    if (adoption == null) {
      return ResponseEntity.status(404).body("Adoption request not found!");
    }

    if (adoption.getStatus() != Status.PENDING) {
      return ResponseEntity.status(400).body("Only pending requests can be updated.");
    }

    adoptionService.changeStatus(adoption, status.getNewStatus());
    return ResponseEntity.ok("Status updated successfully.");
  }

    /**
     * Pending request to the shelter
     * @return
     */
  @PreAuthorize("hasRole('SHELTER')")
  @GetMapping("/shelter/pending")
  public ResponseEntity<?> pendingRequests() {

    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Shelter shelter = shelterService.findByEmail(email);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found!");
    }

    List<AdoptionDTO> pending = adoptionService.getPendingRequestsByShelter(shelter);

    if (pending == null || pending.isEmpty()) {
      return ResponseEntity.status(404).body("No pending requests found for this shelter.");
    }

    return ResponseEntity.ok(pending);

  }

  /**
   * Shelter's accepted requests
   * @return
   */
  @PreAuthorize("hasRole('SHELTER')")
  @GetMapping("/shelter/accepted")
  public ResponseEntity<?> acceptedRequests() {

      String email = SecurityContextHolder.getContext().getAuthentication().getName();
      Shelter shelter = shelterService.findByEmail(email);
      if (shelter == null) {
          return ResponseEntity.status(404).body("Shelter not found!");
      }

      List<AdoptionDTO> accepted = adoptionService.getAcceptedRequestsByShelter(shelter);

      if (accepted == null || accepted.isEmpty()) {
          return ResponseEntity.status(404).body("No accepted requests found for this shelter.");
      }

      return ResponseEntity.ok(accepted);

  }


    /**
     * User pending requests
     * @return
     */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/user/pending")
  public ResponseEntity<?> userPendingAdoptions() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userService.findByEmail(email);
    if (user == null) {
      return ResponseEntity.status(404).body("User not found!");
    }

    List<AdoptionDTO> pendingAdoptions = adoptionService.getUserPendingAdoptions(user);

    return ResponseEntity.ok(pendingAdoptions);
  }

    /**
     * Cancel pending request
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/user/cancel")
    public ResponseEntity<?> cancelUserPendingAdoptions(@RequestBody Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found!");
        }

        Adoption adoption = adoptionService.findAdoptionById(id);

        if(adoption==null){
            return ResponseEntity.status(404).body("Adoption not found!");
        }

        if(adoption.getUser().getId()!=user.getId()){
            return ResponseEntity.status(404).body("This adoption request does not belong to you!");
        }

        if(adoption.getStatus() != Status.PENDING) {
            return ResponseEntity.status(400).body("Only pending requests can be cancelled.");
        }

        adoptionService.changeStatus(adoption, Status.REJECTED);
        return  ResponseEntity.ok("Request cancelled successfully.");


    }

    /**
     * User historic - Accepted adoptions
     * @return
     */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/user/adoptions/historic")
  public ResponseEntity<?> userAcceptedAdoptions() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userService.findByEmail(email);
    if (user == null) {
      return ResponseEntity.status(404).body("User not found!");
    }

    List<AdoptionDTO> acceptedAdoptions = adoptionService.getUserAcceptedAdoptions(user);

    return ResponseEntity.ok(acceptedAdoptions);
  }

    /**
     * User historic - Accepted fosters
     * @return
     */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/user/fosters/historic")
  public ResponseEntity<?> userFosterAdoptions() {
      String email = SecurityContextHolder.getContext().getAuthentication().getName();

      User user = userService.findByEmail(email);
      if (user == null) {
          return ResponseEntity.status(404).body("User not found!");
      }

      List<AdoptionDTO> acceptedFosters = adoptionService.getUserAcceptedFosters(user);

      return ResponseEntity.ok(acceptedFosters);
  }

    /**
     * Admins can see all adoptions
     * @return
     */
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  public ResponseEntity<?> viewAllAdoptions() {
    List<AdoptionDTO> adoptions = adoptionService.getAdoptions();
    if (adoptions.isEmpty()) {
      return ResponseEntity.status(404).body("There are no adoptions");
    }
    return ResponseEntity.ok(adoptions);
  }

    /**
     * Admins can sww all accepted foster requests
     * @return
     */
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/fosters/all")
  public ResponseEntity<?> viewAllFosters() {
    List<AdoptionDTO> adoptions = adoptionService.getFosters();
    if (adoptions.isEmpty()) {
      return ResponseEntity.status(404).body("There are no adoptions");
    }
    return ResponseEntity.ok(adoptions);
  }

  /**
  * To see the adoption/foster historic of a certain animal
  * @param animalId
  * @return
  */
  @PreAuthorize("hasAnyRole('SHELTER','ADMIN')")
  @GetMapping("/animal/{animalId}")
  public ResponseEntity<?> listAnimalHistoric(@PathVariable Long animalId){
      ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(animalId);
      if (animal == null) {
          return ResponseEntity.status(404).body("Animal not found");
      }

      List<AdoptionDTO> historic = adoptionService.getAdoptionsByAnimal(animal);

      if (historic.isEmpty()) {
            return ResponseEntity.status(404).body("This animal was never adopted or fostered");
        }

        return ResponseEntity.status(200).body(historic);

    }


}
