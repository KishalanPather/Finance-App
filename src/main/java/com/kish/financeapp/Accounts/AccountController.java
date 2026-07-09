package com.kish.financeapp.Accounts;

import java.util.List;

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
    public List<AccountResponseDto> getAllAccounts(){
        return accountService.getAllAccounts();
        
    }
    
    @PostMapping()
    public AccountResponseDto createAccount(@RequestBody CreateAccountRequestDto accountRequest){
        return accountService.createAccount(accountRequest);
    }

}
