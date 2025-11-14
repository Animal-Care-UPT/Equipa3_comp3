package AnimalCareCentre.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.service.LostAnimalService;

@RestController
@RequestMapping("/lostanimals/")
public class LostAnimalController {

  private final LostAnimalService lostAnimalService;

  public LostAnimalController(LostAnimalService lostAnimalService) {
    this.lostAnimalService = lostAnimalService;
  }

}
