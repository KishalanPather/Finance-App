package com.kish.financeapp.Accounts;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.kish.financeapp.Accounts.dto.CreateAccountRequestDto;
import com.kish.financeapp.Accounts.dto.AccountResponseDto;
import com.kish.financeapp.Accounts.exceptions.DuplicateAccountException;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository repository) {
        this.accountRepository = repository;
    } 

    public AccountResponseDto createAccount(CreateAccountRequestDto accountRequest) {

        if (accountRepository.existsByName(accountRequest.name())){
            throw new DuplicateAccountException("Account with same name already exists.");
        }

        Account account = new Account();
        account.setName(accountRequest.name());
        account.setAccountType(accountRequest.accountType());
        account.setAvailableBalance(BigDecimal.ZERO); //Accounts start with a zero balance at creation


        Account saved = accountRepository.save(account);
        return new AccountResponseDto(
            saved.getAccountID(),
            saved.getName(),
            saved.getAccountType(),
            saved.getAvailableBalance().toString()
        );

    }

}
