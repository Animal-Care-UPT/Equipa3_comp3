package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.ShelterRepository;

@Service
public class ShelterService {

  private final ShelterRepository shelterRepository;

  public ShelterService(ShelterRepository shelterRepository) {
    this.shelterRepository = shelterRepository;
  }

}
