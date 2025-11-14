package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.LostAnimalRepository;

@Service
public class LostAnimalService {

  private final LostAnimalRepository lostAnimalRepository;

  public LostAnimalService(LostAnimalRepository lostAnimalRepository) {
    this.lostAnimalRepository = lostAnimalRepository;
  }

}
