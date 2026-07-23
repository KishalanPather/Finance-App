package com.kish.financeapp.Accounts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kish.financeapp.Accounts.dtos.AccountResponseDto;
import com.kish.financeapp.Accounts.dtos.CreateAccountRequestDto;
import com.kish.financeapp.Accounts.enums.AccountStatus;
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

        Account account = new Account(
            null, 
            accountRequest.name(),
            accountRequest.accountType(),
            BigDecimal.ZERO,
            AccountStatus.ACTIVE

        );

        Account saved = accountRepository.save(account);
        return new AccountResponseDto(
            saved.getAccountID(),
            saved.getName(),
            saved.getAccountType(),
            saved.getAvailableBalance().toString()
        );

    }

    public List<AccountResponseDto> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();

         return accounts.stream().map(account -> new AccountResponseDto(
            account.getAccountID(),
            account.getName(),
            account.getAccountType(),
            account.getAvailableBalance().toString()
         ))
         .toList();
            
    }

}
