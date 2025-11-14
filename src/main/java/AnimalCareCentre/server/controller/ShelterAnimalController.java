package AnimalCareCentre.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import AnimalCareCentre.server.service.ShelterAnimalService;

@Controller
@RequestMapping("/accounts/")
public class ShelterAnimalController {

  private final ShelterAnimalService shelterAnimalService;

  public ShelterAnimalController(ShelterAnimalService shelterAnimalService) {
    this.shelterAnimalService = shelterAnimalService;
  }

}
