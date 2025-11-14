package AnimalCareCentre.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import AnimalCareCentre.server.service.DonationService;

@Controller
@RequestMapping("/donations/")
public class DonationController {

  private final DonationService donationService;

  public DonationController(DonationService donationService) {
    this.donationService = donationService;
  }

}
