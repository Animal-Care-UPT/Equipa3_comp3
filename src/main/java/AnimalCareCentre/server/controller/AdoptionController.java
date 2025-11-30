package AnimalCareCentre.server.controller;

import AnimalCareCentre.server.dto.AdoptionRequestDTO;
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


    // Pedido de adoção
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

    // Alterar estado do pedido
    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long adoptionId, @RequestParam Status status) {

        Adoption adoption = adoptionService.findAdoptionById(adoptionId);
        if (adoption == null) {
            return ResponseEntity.status(404).body("That Id doesn't correspond to any adoption!");
        }

        Adoption changedAdoption = adoptionService.changeStatus(adoption, status);

        return ResponseEntity.ok().body(changedAdoption);
    }


    // Pedidos pendentes de um shelter
    @GetMapping("/pending")
    public List<Adoption> pendingRequests(@RequestParam Long shelterId) {
        return adoptionService.getPendingRequestsByShelter(shelterId);
    }


    @GetMapping("/user/adoptions")
    public ResponseEntity<?> userAdoptions(@RequestParam String email) {
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).body("That Id doesn't correspond to any user!");
        }

        List<Adoption> userAdoptions = adoptionService.getUserAdoptions(user);

        return ResponseEntity.ok().body(userAdoptions);
    }

}

