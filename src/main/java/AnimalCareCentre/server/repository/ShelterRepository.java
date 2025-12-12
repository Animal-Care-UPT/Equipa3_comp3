package AnimalCareCentre.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import AnimalCareCentre.server.enums.Status;
import AnimalCareCentre.server.model.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

  @Query("SELECT s FROM Shelter s WHERE " +
      "s.status = :status AND " +
      "(:keyword IS NULL OR :keyword = '' OR (" +
      "s.name LIKE :keyword OR s.location LIKE :keyword))")
  public List<Shelter> findByStatusAndKeyword(Status status, String keyword);

  @Query("SELECT s FROM Shelter s WHERE " +
      ":keyword IS NULL OR :keyword = '' OR " +
      "(s.name LIKE :keyword OR s.location LIKE :keyword)")
  public List<Shelter> findByKeyword(String keyword);

  public List<Shelter> findByStatus(Status status);

  public Shelter findByEmail(String email);
}
