package AnimalCareCentre.server.repository;

import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterDonation;
import AnimalCareCentre.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import AnimalCareCentre.server.model.Donation;

import java.util.List;

public interface ShelterDonationRepository extends JpaRepository<ShelterDonation, Long> {
    List<ShelterDonation> findByDonorOrderByDonationDateDesc(User donor);
    List<ShelterDonation> findByShelterOrderByDonationDateDesc(Shelter shelter);
}
