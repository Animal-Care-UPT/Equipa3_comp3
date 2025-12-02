package AnimalCareCentre.server.controller;

import AnimalCareCentre.server.enums.AnimalColor;
import AnimalCareCentre.server.enums.AnimalType;
import AnimalCareCentre.server.model.Account;
import AnimalCareCentre.server.model.LostAnimal;
import AnimalCareCentre.server.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import AnimalCareCentre.server.service.LostAnimalService;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/lostandfound/")
public class LostAnimalController {

  private final LostAnimalService lostAnimalService;
  private final AccountService accountService;

  public LostAnimalController(LostAnimalService lostAnimalService, AccountService accountService) {
    this.lostAnimalService = lostAnimalService;
      this.accountService = accountService;
  }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/showrescuedanimals")
    public ResponseEntity<?> showRescuedAnimals() {
        List<LostAnimal> results = lostAnimalService.findRescuedAnimals();
        results.sort(Comparator.comparing(LostAnimal::getLocation));
        if (!results.isEmpty()) {
            return ResponseEntity.ok().body(results);
        }
        return ResponseEntity.status(404).body("There are no registered animals as rescued");
    }
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/showlostanimals")
  public ResponseEntity<?> showLostAnimals() {
      List<LostAnimal> results = lostAnimalService.findLostAnimals();
      results.sort(Comparator.comparing(LostAnimal::getLocation));
      if (!results.isEmpty()) {
          return ResponseEntity.ok().body(results);
      }
      return ResponseEntity.status(404).body("There are no registered animals in Lost and Found");
  }
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/showanimalsbyaccount")
  public ResponseEntity<?> showLostAnimalsByAccount(){
      Account account = accountService.findAccount( SecurityContextHolder.getContext().getAuthentication().getName());


      List<LostAnimal> results = lostAnimalService.findLostAnimalsByAccount(account.getId());
      results.sort(Comparator.comparing(LostAnimal::getLocation));
      if (!results.isEmpty()) {
          return ResponseEntity.ok().body(results);
      }
      return ResponseEntity.status(404).body("There are no registered lost animals to this account");
  }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/showByLocation")
    public ResponseEntity<?> showLostAnimalsByLocation(@NotNull  @RequestParam String location){

        List<LostAnimal> results = lostAnimalService.findByLocation(location);
        if (!results.isEmpty()) {
            return ResponseEntity.ok().body(results);
        }
        return ResponseEntity.status(404).body("There are no registered lost animals in this location");
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/showByType")
    public ResponseEntity<?> showLostAnimalsByType(@NotNull  @RequestParam AnimalType animalType){

        List<LostAnimal> results = lostAnimalService.searchByType(animalType);
        if (!results.isEmpty()) {
            return ResponseEntity.ok().body(results);
        }
        return ResponseEntity.status(404).body("There are no registered lost animals in this location");
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/showByColor")
    public ResponseEntity<?> showLostAnimalsByColor(@NotNull  @RequestParam AnimalColor animalColor){

        List<LostAnimal> results = lostAnimalService.searchByColor(animalColor);
        if (!results.isEmpty()) {
            return ResponseEntity.ok().body(results);
        }
        return ResponseEntity.status(404).body("There are no registered lost animals in this location");
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> registerLostAnimal(@Valid @RequestBody LostAnimal lostAnimal){
      Account acc = accountService.findAccount(SecurityContextHolder.getContext().getAuthentication().getName());
      lostAnimal.setAccount(acc);

      lostAnimalService.registerLostAnimal(lostAnimal);
      return ResponseEntity.status(200).body(lostAnimal);


    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable long id){


        lostAnimalService.deleteById(id);
        return ResponseEntity.status(200).body(id);


    }


}
