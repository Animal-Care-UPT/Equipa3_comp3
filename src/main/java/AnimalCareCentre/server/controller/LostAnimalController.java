package AnimalCareCentre.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import AnimalCareCentre.server.service.LostAnimalService;

@Controller
@RequestMapping("/lostanimals/")
public class LostAnimalController {

  private final LostAnimalService lostAnimalService;

  public LostAnimalController(LostAnimalService lostAnimalService) {
    this.lostAnimalService = lostAnimalService;
  }

}
