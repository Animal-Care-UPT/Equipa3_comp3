package AnimalCareCentre.server.service;

import AnimalCareCentre.server.enums.AnimalType;
import AnimalCareCentre.server.enums.AnimalColor;
import AnimalCareCentre.server.enums.AnimalSize;
<<<<<<< HEAD
=======
import AnimalCareCentre.server.enums.District;
import AnimalCareCentre.server.model.Account;
>>>>>>> main
import AnimalCareCentre.server.model.LostAnimal;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.LostAnimalRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


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

  public List<LostAnimal> findByLocation(District location){
      return lostAnimalRepository.findByLocation(location);
  }
  public LostAnimal findLostAnimalById(long id){
      return lostAnimalRepository.findById(id).orElse(null);

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

    public void addImagePath(LostAnimal animal, String imageUrl) {
        animal.getImages().add(imageUrl);
        lostAnimalRepository.save(animal);
    }
}
