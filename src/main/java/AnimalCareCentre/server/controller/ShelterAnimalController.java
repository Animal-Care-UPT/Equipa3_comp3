package AnimalCareCentre.server.controller;

import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.service.ShelterService;
import AnimalCareCentre.server.service.ShelterAnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  @PostMapping("/register")
  public ResponseEntity<?> registerShelterAnimal(@RequestBody ShelterAnimal shelterAnimal) {
    if (shelterAnimal.getShelter() == null || shelterAnimal.getShelter().getId() == 0 ||
        shelterAnimal.getColor() == null || shelterAnimal.getName() == null ||
        shelterAnimal.getListedFor() == null || shelterAnimal.getRace() == null ||
        shelterAnimal.getSize() == null || shelterAnimal.getType() == null ||
        shelterAnimal.getGender() == null) {
      return ResponseEntity.badRequest().body("All fields are required!");
    }

    Shelter shelter = shelterService.findById(shelterAnimal.getShelter().getId());

    if (shelter == null) {
      return ResponseEntity.status(404).body("The shelter doesn't exist!");
    }

    shelterAnimal.setShelter(shelter);
    ShelterAnimal savedAnimal = shelterAnimalService.registerShelterAnimal(shelterAnimal);
    return ResponseEntity.status(201).body(savedAnimal);
  }

}
