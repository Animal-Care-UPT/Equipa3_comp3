package AnimalCareCentre.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import AnimalCareCentre.server.service.ShelterService;

@Controller
@RequestMapping("/accounts/")
public class ShelterController {

  private final ShelterService shelterService;

  public ShelterController(ShelterService shelterService) {
    this.shelterService = shelterService;
  }

}
