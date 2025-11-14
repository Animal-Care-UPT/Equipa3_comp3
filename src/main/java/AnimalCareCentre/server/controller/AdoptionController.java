package AnimalCareCentre.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.service.AdoptionService;

@RestController
@RequestMapping("/adoptions/")
public class AdoptionController {

  private final AdoptionService adoptionService;

  public AdoptionController(AdoptionService adoptionService) {
    this.adoptionService = adoptionService;
  }

}

