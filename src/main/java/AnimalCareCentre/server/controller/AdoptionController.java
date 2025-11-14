package AnimalCareCentre.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import AnimalCareCentre.server.service.AdoptionService;

@Controller
@RequestMapping("/adoptions/")
public class AdoptionController {

  private final AdoptionService adoptionService;

  public AdoptionController(AdoptionService adoptionService) {
    this.adoptionService = adoptionService;
  }

}

