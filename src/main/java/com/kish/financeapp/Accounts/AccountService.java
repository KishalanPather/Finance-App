package com.kish.financeapp.Accounts;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kish.financeapp.Accounts.dto.CreateAccountRequestDto;
import com.kish.financeapp.Accounts.dto.CreateAccountResponseDto;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository repository) {
        this.accountRepository = repository;
    } 

    public CreateAccountResponseDto createAccount(CreateAccountRequestDto accountRequest) {
        Account account = new Account();
        account.setName(accountRequest.getName());
        account.setAccountType(accountRequest.getAccountType());
        account.setAvailableBalance(BigDecimal.ZERO); //Accounts start with a zero balance at creation

        accountRepository.
        return account;
    }

}
