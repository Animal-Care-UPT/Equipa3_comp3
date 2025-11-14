package AnimalCareCentre.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.service.ShelterService;

@RestController
@RequestMapping("/accounts/")
public class ShelterController {

  private final ShelterService shelterService;

  public ShelterController(ShelterService shelterService) {
    this.shelterService = shelterService;
  }

}
