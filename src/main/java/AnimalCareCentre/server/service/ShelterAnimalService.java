package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.ShelterAnimalRepository;

@Service
public class ShelterAnimalService {

  private final ShelterAnimalRepository shelterAnimalRepository;

  public ShelterAnimalService(ShelterAnimalRepository shelterAnimalRepository) {
    this.shelterAnimalRepository = shelterAnimalRepository;
  }

}
