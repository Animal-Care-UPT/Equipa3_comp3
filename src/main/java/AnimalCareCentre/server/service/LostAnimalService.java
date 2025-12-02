package AnimalCareCentre.server.service;

import AnimalCareCentre.server.enums.AnimalType;
import AnimalCareCentre.server.enums.AnimalColor;
import AnimalCareCentre.server.enums.AnimalSize;
import AnimalCareCentre.server.model.Account;
import AnimalCareCentre.server.model.LostAnimal;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.LostAnimalRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class LostAnimalService {

  private final LostAnimalRepository lostAnimalRepository;

  public LostAnimalService(LostAnimalRepository lostAnimalRepository) {
    this.lostAnimalRepository = lostAnimalRepository;
  }

  public List<LostAnimal> findLostAnimalsByAccount(long accountId){
      return lostAnimalRepository.findByAccountId(accountId);
  }

  public List<LostAnimal> findLostAnimals(){
        return lostAnimalRepository.findAll();
  }
  public List<LostAnimal> findRescuedAnimals(){
      return lostAnimalRepository.findByIsLost(false);
  }

  public List<LostAnimal> findByLocation(String location){
      return lostAnimalRepository.findByLocation(location);
  }

  public void registerLostAnimal(LostAnimal lostAnimal){
      lostAnimalRepository.save(lostAnimal);
  }


  public List<LostAnimal> searchBySize(AnimalSize size) {
        List<LostAnimal> lostAnimals = lostAnimalRepository.findBySize(size);
        lostAnimals.sort(Comparator.comparing(LostAnimal::getType).thenComparing(LostAnimal::getLocation));
        return lostAnimals;
  }

  public List<LostAnimal> searchByColor(AnimalColor color) {
        List<LostAnimal> lostAnimals = lostAnimalRepository.findByColor(color );
        lostAnimals.sort(Comparator.comparing(LostAnimal::getType).thenComparing(LostAnimal::getLocation));
        return lostAnimals;
  }

  public List<LostAnimal> searchByType(AnimalType type) {
        List<LostAnimal> lostAnimals = lostAnimalRepository.findByType(type);
        lostAnimals.sort(Comparator.comparing(LostAnimal::getLocation).thenComparing(LostAnimal::getRace));
        return lostAnimals;
    }

    public void delete(LostAnimal lostAnimal) {
      lostAnimalRepository.delete(lostAnimal);
    }

    public void deleteById(long lostAnimalId) {
      lostAnimalRepository.deleteById(lostAnimalId);
    }
}
