package AnimalCareCentre.server.controller;

import AnimalCareCentre.server.dto.AdoptionChangeStatusDTO;
import AnimalCareCentre.server.dto.AdoptionRequestDTO;
import AnimalCareCentre.server.dto.AdoptionResponseDTO;
import AnimalCareCentre.server.dto.AdoptionsUserDTO;
import AnimalCareCentre.server.model.Adoption;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.enums.Status;
import AnimalCareCentre.server.service.AdoptionService;
import AnimalCareCentre.server.service.ShelterAnimalService;
import AnimalCareCentre.server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/adoptions/")
public class AdoptionController {

    private final AdoptionService adoptionService;
    private final UserService userService;
    private final ShelterAnimalService shelterAnimalService;


    public AdoptionController(AdoptionService adoptionService, ShelterAnimalService shelterAnimalService, UserService userService) {

        this.adoptionService = adoptionService;
        this.userService = userService;
        this.shelterAnimalService = shelterAnimalService;
    }


    // Adoption request
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/request")
    public ResponseEntity<?> requestAdoption(@Valid @RequestBody AdoptionRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).body("This user isn't registered!");
        }

        Adoption adoption = adoptionService.requestAdoption(user, dto.getAnimalId(), dto.getAdoptionType());
        return ResponseEntity.status(201).body(adoption);
    }

    // Change the status of an adoption/foster request
    @PreAuthorize("hasRole('SHELTER')")
    @PutMapping("/change-status")
    public ResponseEntity<?> changeStatus(@RequestBody AdoptionChangeStatusDTO status) {

        Adoption adoption = adoptionService.findAdoptionById(status.getAdoptionId());

        if (adoption == null) {
            return ResponseEntity.status(404).body("Adoption request not found!");
        }

        // Validation to make sure only the pending requests have their status changed
        if (adoption.getStatus() != Status.PENDING) {
            return ResponseEntity.status(400).body("Only pending requests can be updated.");
        }

        adoptionService.changeStatus(adoption, status.getNewStatus());

        return ResponseEntity.ok("Status updated successfully.");
    }



    // Pending request to the shelter
    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/pending")
    public List<AdoptionResponseDTO> pendingRequests(@RequestParam Long shelterId) {
        return adoptionService.getPendingRequestsByShelter(shelterId); //spring return automatically the 200 ok
    }

    // User historic
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/adoptions")
    public ResponseEntity<?> userAdoptions() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found!");
        }

        List<AdoptionsUserDTO> userAdoptions = adoptionService.getUserAdoptions(user);

        return ResponseEntity.ok(userAdoptions);
    }


}

