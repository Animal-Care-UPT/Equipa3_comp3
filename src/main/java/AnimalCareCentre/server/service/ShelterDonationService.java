package AnimalCareCentre.server.service;

import AnimalCareCentre.server.dto.ShelterDonationDTO;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterDonation;
import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.repository.ShelterRepository;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.ShelterDonationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShelterDonationService {

  private final ShelterDonationRepository donationRepository;
  private final ShelterRepository shelterRepository;

  public ShelterDonationService(ShelterDonationRepository donationRepository, ShelterRepository shelterRepository) {
    this.donationRepository = donationRepository;
    this.shelterRepository = shelterRepository;
  }

    /**
     * New donations to shelters
     * @param user
     * @param shelterId
     * @param donationAmount
     * @return
     */
  public ShelterDonation newShelterDonation(User user, Long shelterId, float donationAmount) {

      Shelter shelter = shelterRepository.findById(shelterId).orElse(null);
      if (shelter == null) {
          return null;
      }
      else{
          ShelterDonation donation = new ShelterDonation(user, shelter, donationAmount);
          donationRepository.save(donation);
          return donation;
      }
  }

    /**
     * To allow a shelter to see its donations
     * @param shelter
     * @return
     */
  public List<ShelterDonationDTO> getShelterDonations(Shelter shelter) {
      List<ShelterDonation> donations = donationRepository.findByShelterOrderByDonationDateDesc(shelter);

      return donations.stream().map(d-> {
          ShelterDonationDTO dto = new ShelterDonationDTO();
          dto.setUserId(d.getUser().getId());
          dto.setUserName(d.getUser().getName());
          dto.setUserEmail(d.getUser().getEmail());
          dto.setShelterId(shelter.getId());
          dto.setShelterName(shelter.getName());
          dto.setAmount(d.getAmount());
          dto.setDonationDate(d.getDonationDate());
          return dto;
      }).toList();

  }

    /**
     * To allow a user to see their donations
     * @param user
     * @return
     */
  public List<ShelterDonationDTO> getUserDonations(User user) {
      List<ShelterDonation> donations = donationRepository.findByUserOrderByDonationDateDesc(user);

      return donations.stream().map(d->{
          ShelterDonationDTO dto = new ShelterDonationDTO();
          dto.setUserId(user.getId());
          dto.setUserName(user.getName());
          dto.setUserEmail(user.getEmail());
          dto.setShelterId(d.getShelter().getId());
          dto.setShelterName(d.getShelter().getName());
          dto.setAmount(d.getAmount());
          dto.setDonationDate(d.getDonationDate());
          return dto;
      }).toList();
  }

}
