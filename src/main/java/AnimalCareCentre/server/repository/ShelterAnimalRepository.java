package AnimalCareCentre.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterAnimal;

public interface ShelterAnimalRepository extends JpaRepository<ShelterAnimal, Long> {

  @Query("SELECT a FROM ShelterAnimal a WHERE " +
      "a.status = :status AND " +
      "(:keyword IS NULL OR :keyword = '' OR (" +
      "a.name LIKE :keyword OR a.race LIKE :keyword OR " +
      "CAST(a.type AS string) LIKE :keyword OR " +
      "CAST(a.gender AS string) LIKE :keyword OR " +
      "CAST(a.size AS string) LIKE :keyword OR " +
      "CAST(a.color AS string) LIKE :keyword)) AND " +
      "(:type IS NULL OR a.type = :type) AND " +
      "(:gender IS NULL OR a.gender = :gender) AND " +
      "(:adoptionType IS NULL OR a.adoptionType = :adoptionType)")
  List<ShelterAnimal> searchWithFilters(
      @Param("keyword") String keyword,
      @Param("status") Status status,
      @Param("type") AnimalType type,
      @Param("gender") AnimalGender gender,
      @Param("adoptionType") AdoptionType adoptionType);

  List<ShelterAnimal> findByShelter(Shelter shelter);
  List<ShelterAnimal> findByStatusAndShelter(Status status, Shelter shelter);
}
