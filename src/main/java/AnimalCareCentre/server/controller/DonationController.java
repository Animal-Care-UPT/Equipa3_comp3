package AnimalCareCentre.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.service.DonationService;

@RestController
@RequestMapping("/donations/")
public class DonationController {

  private final DonationService donationService;

  public DonationController(DonationService donationService) {
    this.donationService = donationService;
  }

}
