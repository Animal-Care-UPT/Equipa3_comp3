package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.AdoptionRepository;

@Service
public class AdoptionService {

  private final AdoptionRepository adoptionRepository;

  public AdoptionService(AdoptionRepository adoptionRepository) {
    this.adoptionRepository = adoptionRepository;
  }

}
