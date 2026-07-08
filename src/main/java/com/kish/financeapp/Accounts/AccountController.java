package com.kish.financeapp.Accounts;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kish.financeapp.Accounts.dto.CreateAccountRequestDto;
import com.kish.financeapp.Accounts.dto.AccountResponseDto;

@RestController
@RequestMapping("/api/account/v1")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }


    @GetMapping()
    public String getAccounts(){
        return "Example: Account 1, Account2, etc";
    }
    
    @PostMapping()
    public AccountResponseDto createAccount(@RequestBody CreateAccountRequestDto accountRequest){
        return accountService.createAccount(accountRequest);
    }

}
