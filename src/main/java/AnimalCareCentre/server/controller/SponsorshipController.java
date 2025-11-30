// package AnimalCareCentre.server.controller;
//
// import java.util.List;
//
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import AnimalCareCentre.server.dto.SponsorshipDTO;
// import AnimalCareCentre.server.model.ShelterAnimal;
// import AnimalCareCentre.server.model.Sponsorship;
// import AnimalCareCentre.server.model.User;
// import AnimalCareCentre.server.service.ShelterAnimalService;
// import AnimalCareCentre.server.service.SponsorshipService;
// import AnimalCareCentre.server.service.UserService;
// import jakarta.validation.Valid;
//
// @RestController
// @RequestMapping("/sponsorships/")
// public class SponsorshipController {
//
//   private final SponsorshipService sponsorshipService;
//   private final UserService userService;
//   private final ShelterAnimalService shelterAnimalService;
//
//   public SponsorshipController(SponsorshipService sponsorshipService, UserService userService,
//       ShelterAnimalService shelterAnimalService) {
//     this.sponsorshipService = sponsorshipService;
//     this.userService = userService;
//     this.shelterAnimalService = shelterAnimalService;
//   }
//
//   @PostMapping("/create")
//   public ResponseEntity<?> createSponsorShip(@Valid @RequestBody SponsorshipDTO sponsorship) {
//     String email = SecurityContextHolder.getContext().getAuthentication().getName();
//     User user = userService.findByEmail(email);
//     ShelterAnimal animal = shelterAnimalService.findById(sponsorship.getId());
//     Float amount = sponsorship.getAmount();
//     Sponsorship sponsor = sponsorshipService.createSponsorShip(user, animal, amount);
//     return ResponseEntity.status(201).body(sponsor);
//   }
//
//   @GetMapping("/search/all")
//   public ResponseEntity<?> getSponsorShips(){
//     List<Sponsorship> results = sponsorshipService.searchAll();
//     if (!results.isEmpty()){
//       return ResponseEntity.ok().body(results);
//     }
//     return ResponseEntity.status(404).body("There are no sponsoships registered");
//   }
//
// }
