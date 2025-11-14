package AnimalCareCentre.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AnimalCareCentre.server.model.Account;
import AnimalCareCentre.server.service.AccountService;

@RestController
@RequestMapping("/accounts/")
public class AccountController {

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping
  public Account create(@RequestBody Account account) {
    return accountService.createAccount(account);
  }

}
