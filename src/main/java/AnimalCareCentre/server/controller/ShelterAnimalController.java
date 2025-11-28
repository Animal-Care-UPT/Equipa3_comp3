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

  @GetMapping("/search/all")
  public ResponseEntity<?> getAnimals() {
    List<ShelterAnimal> results = shelterAnimalService.searchAll();
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no registered animals");
  }

  @GetMapping("/search")
  public ResponseEntity<?> getAnimalsByKeyword(@NotBlank @RequestParam String keyword) {
    List<ShelterAnimal> results = shelterAnimalService.searchByKeyword(keyword);
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("No matching animals!");
  }

  @GetMapping("/search/shelter")
  public ResponseEntity<?> getShelterAnimals(@NotNull @RequestParam long id) {
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

  @GetMapping("/search/gender")
  public ResponseEntity<?> getAnimalsByGender(@NotNull @RequestParam AnimalGender gender) {
    List<ShelterAnimal> animals = shelterAnimalService.searchByGender(gender);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("There are no animals of this gender!");
  }

  @GetMapping("/search/type")
  public ResponseEntity<?> getAnimalsByType(@NotNull @RequestParam AnimalType type) {
    List<ShelterAnimal> animals = shelterAnimalService.searchByType(type);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("There are no animals of the chosen type!");
  }

  @GetMapping("/search/foster")
  public ResponseEntity<?> getFosterAnimals() {
    List<ShelterAnimal> animals = shelterAnimalService.searchFosterAnimals();
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("There are no animals available for foster!");
  }

  @GetMapping("/search/adoption")
  public ResponseEntity<?> getAdoptionAnimals() {
    List<ShelterAnimal> animals = shelterAnimalService.searchAdoptionAnimals();
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("There are no animals available for adoption!");
  }

}
