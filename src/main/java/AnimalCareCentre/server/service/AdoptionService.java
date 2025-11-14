package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

@Service
public class AdoptionService {

  private final AdoptionService adoptionService;

  public AdoptionService(AdoptionService adoptionService) {
    this.adoptionService = adoptionService;
  }

}
