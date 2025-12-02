package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.DonationRepository;

@Service
public class DonationService {

  private final DonationRepository donationRepository;

  public DonationService(DonationRepository donationRepository) {
    this.donationRepository = donationRepository;
  }
  
  public DonationService createDonation(Float amount){
    return null;
  }
}
