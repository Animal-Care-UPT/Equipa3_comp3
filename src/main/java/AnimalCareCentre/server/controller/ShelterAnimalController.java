package AnimalCareCentre.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.service.ShelterAnimalService;

@RestController
@RequestMapping("/accounts/")
public class ShelterAnimalController {

  private final ShelterAnimalService shelterAnimalService;

  public ShelterAnimalController(ShelterAnimalService shelterAnimalService) {
    this.shelterAnimalService = shelterAnimalService;
  }

}
