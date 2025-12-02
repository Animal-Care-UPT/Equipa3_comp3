package AnimalCareCentre.server.repository;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.Account;
import AnimalCareCentre.server.model.LostAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LostAnimalRepository extends JpaRepository<LostAnimal, Long> {

   public List<LostAnimal> findByAccountId(long accountId);

   public List<LostAnimal> findByLocation(String location);

    public List<LostAnimal> findByIsLost(boolean isLost);

    public List<LostAnimal> findByGender(AnimalGender gender);

    public List<LostAnimal> findBySize(AnimalSize size);

    public List<LostAnimal> findByColor(AnimalColor color);

    public List<LostAnimal> findByType(AnimalType type);


}