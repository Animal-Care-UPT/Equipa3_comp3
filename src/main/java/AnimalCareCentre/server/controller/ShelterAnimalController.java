package AnimalCareCentre.server.controller;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.service.ShelterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import AnimalCareCentre.server.service.ShelterAnimalService;

import java.util.List;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shelteranimals/")
public class ShelterAnimalController {

  private final ShelterAnimalService shelterAnimalService;
  private final ShelterService shelterService;

  public ShelterAnimalController(ShelterAnimalService shelterAnimalService,
      ShelterService shelterService) {
    this.shelterAnimalService = shelterAnimalService;
    this.shelterService = shelterService;
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PostMapping("/register")
  public ResponseEntity<?> registerShelterAnimal(@Valid @RequestBody ShelterAnimal shelterAnimal) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Shelter shelter = shelterService.findByEmail(email);
    if (shelter == null) {
      return ResponseEntity.status(404).body("The shelter doesn't exist!");
    }

    shelterAnimal.setShelter(shelter);
    ShelterAnimal savedAnimal = shelterAnimalService.registerShelterAnimal(shelterAnimal);
    return ResponseEntity.status(201).body(savedAnimal);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/search/all")
  public ResponseEntity<?> getAnimals() {
    List<ShelterAnimal> results = shelterAnimalService.searchAll();
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no registered animals");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/search")
  public ResponseEntity<?> getAnimalsByKeyword(@NotBlank @RequestParam String keyword) {
    List<ShelterAnimal> results = shelterAnimalService.searchByKeyword(keyword);
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("No matching animals!");
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/search/shelter")
  public ResponseEntity<?> getShelterAnimals(@NotNull @RequestParam Long id) {
    Shelter shelter = shelterService.findById(id);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found!");
    }

    List<ShelterAnimal> animals = shelterAnimalService.searchByShelter(shelter);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("No available Animals!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @GetMapping("/search/self")
  public ResponseEntity<?> getLoggedShelterAnimals() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Shelter shelter = shelterService.findByEmail(email);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found!");
    }

    List<ShelterAnimal> animals = shelterAnimalService.searchByShelter(shelter);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("No available Animals!");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/search/shelter/available")
  public ResponseEntity<?> getAvailableShelterAnimals(@NotNull @RequestParam long id) {
    Shelter shelter = shelterService.findById(id);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found!");
    }

    List<ShelterAnimal> animals = shelterAnimalService.searchAvailableByShelter(shelter);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("This shelter has no available animals!");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/search/gender")
  public ResponseEntity<?> getAnimalsByGender(@NotNull @RequestParam AnimalGender gender) {
    List<ShelterAnimal> animals = shelterAnimalService.searchByGender(gender);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("There are no animals of this gender!");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/search/size")
  public ResponseEntity<?> getAnimalsBySize(@NotNull @RequestParam AnimalSize size) {
    List<ShelterAnimal> animals = shelterAnimalService.searchBySize(size);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("There are no animals of this size!");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/search/color")
  public ResponseEntity<?> getAnimalsByColor(@NotNull @RequestParam AnimalColor color) {
    List<ShelterAnimal> animals = shelterAnimalService.searchByColor(color);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("There are no animals of this color!");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/search/type")
  public ResponseEntity<?> getAnimalsByType(@NotNull @RequestParam AnimalType type) {
    List<ShelterAnimal> animals = shelterAnimalService.searchByType(type);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("There are no animals of the chosen type!");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/search/foster")
  public ResponseEntity<?> getFosterAnimals() {
    List<ShelterAnimal> animals = shelterAnimalService.searchFosterAnimals();
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("There are no animals available for foster!");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/search/adoption")
  public ResponseEntity<?> getAdoptionAnimals() {
    List<ShelterAnimal> animals = shelterAnimalService.searchAdoptionAnimals();
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("There are no animals available for adoption!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PutMapping("/vacination")
  public ResponseEntity<?> changeVacination(@NotNull @RequestParam Long id) {
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal != null) {
      shelterAnimalService.changeVacination(animal);
      return ResponseEntity.ok("Changed vacination with success!");
    }
    return ResponseEntity.status(404).body("Animal not found!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PutMapping("/age")
  public ResponseEntity<?> changeVacination(@NotNull @RequestParam Long id, @NotNull @RequestParam Integer age) {
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal != null) {
      shelterAnimalService.changeAge(animal, age);
      return ResponseEntity.ok("Changed age with success!");
    }
    return ResponseEntity.status(404).body("Animal not found!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PutMapping("/status")
  public ResponseEntity<?> changeStatus(@NotNull @RequestParam Long id) {
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal != null) {
      shelterAnimalService.changeStatus(animal);
      return ResponseEntity.ok("Changed status with success!");
    }
    return ResponseEntity.status(404).body("Animal not found!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PutMapping("/adoptiontype")
  public ResponseEntity<?> changeAdoptionType(@NotNull @RequestParam Long id) {
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal != null) {
      shelterAnimalService.changeAdoptionType(animal);
      return ResponseEntity.ok("Changed adoption type with success!");
    }
    return ResponseEntity.status(404).body("Animal not found!");
  }

}
