package com.kish.financeapp.Accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @GetMapping()
    public String getAccounts(){
        return "Example: Account 1, Account2, etc";
    }
    
    @PostMapping()
    public Account createAccount(@RequestBody Account account){
        return accountService.createAccount(account);
    }

}
