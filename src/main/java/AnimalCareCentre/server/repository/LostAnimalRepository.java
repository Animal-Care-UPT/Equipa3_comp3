package AnimalCareCentre.server.repository;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.Account;
import AnimalCareCentre.server.model.LostAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

import AnimalCareCentre.server.model.LostAnimal;

import java.util.List;

public interface LostAnimalRepository extends JpaRepository<LostAnimal, Long> {

   public List<LostAnimal> findLostAnimalByAccount(Account account);

   public List<LostAnimal> findByLocation(String location);

    public List<LostAnimal> findByIsLost(boolean isLost);

    public List<LostAnimal> findByGender(AnimalGender gender);

    public List<LostAnimal> findBySize(AnimalSize size);

    public List<LostAnimal> findByColor(AnimalColor color);

    public List<LostAnimal> findByType(AnimalType type);


}