package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.SponsorshipRepository;

@Service
public class SponsorshipService {

  private final SponsorshipRepository sponsorshipRepository;

  public SponsorshipService(SponsorshipRepository sponsorshipRepository) {
    this.sponsorshipRepository = sponsorshipRepository;
  }

}
