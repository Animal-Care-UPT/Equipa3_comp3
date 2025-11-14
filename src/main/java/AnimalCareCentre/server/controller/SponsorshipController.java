package AnimalCareCentre.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import AnimalCareCentre.server.service.SponsorshipService;

@Controller
@RequestMapping("/accounts/")
public class SponsorshipController {

  private final SponsorshipService sponsorshipService;

  public SponsorshipController(SponsorshipService sponsorshipService) {
    this.sponsorshipService = sponsorshipService;
  }

}
