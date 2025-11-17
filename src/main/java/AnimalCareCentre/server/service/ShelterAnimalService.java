package AnimalCareCentre.server.service;

import AnimalCareCentre.server.model.ShelterAnimal;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.ShelterAnimalRepository;

@Service
public class ShelterAnimalService {

  private final ShelterAnimalRepository shelterAnimalRepository;

  public ShelterAnimalService(ShelterAnimalRepository shelterAnimalRepository) {
    this.shelterAnimalRepository = shelterAnimalRepository;
  }

  public ShelterAnimal registerShelterAnimal(ShelterAnimal shelterAnimal) {
      return shelterAnimalRepository.save(shelterAnimal);
  }

}
