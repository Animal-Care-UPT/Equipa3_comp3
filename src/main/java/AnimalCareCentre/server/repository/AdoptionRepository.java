package AnimalCareCentre.server.repository;

import AnimalCareCentre.server.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import AnimalCareCentre.server.model.Adoption;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.enums.*;
import java.util.List;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    List<Adoption> findByUser(User user);
    List<Adoption> findByAnimal(ShelterAnimal animal);
    List<Adoption> findByAnimal_ShelterAndStatus(Shelter shelter, Status status);
    List<Adoption> findByAdoptionType(AdoptionType type);
    List<Adoption> findByUserAndStatus(User user, Status status);
    List<Adoption> findByType(AdoptionType type);

}
