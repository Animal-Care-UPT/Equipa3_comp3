package AnimalCareCentre.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterAnimal;

public interface ShelterAnimalRepository extends JpaRepository<ShelterAnimal, Long> {

  @Query("SELECT s FROM ShelterAnimal s WHERE s.listedFor != :status AND (" +
      "s.name LIKE :search OR s.race LIKE :search OR " +
      "CAST(s.type AS string) LIKE :search OR " +
      "CAST(s.size AS string) LIKE :search OR " +
      "CAST(s.color AS string) LIKE :search OR " +
      "s.name LIKE :search)")
  public List<ShelterAnimal> findByKeyword(@Param("search") String search, @Param("status") Status status);
  public List<ShelterAnimal> findByGender(AnimalGender gender);
  public List<ShelterAnimal> findByType(AnimalType type);
  public List<ShelterAnimal> findByShelter(Shelter shelter);
  public List<ShelterAnimal> findByStatusAndShelter(Status status, Shelter shelter);

}
