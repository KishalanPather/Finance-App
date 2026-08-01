package com.kish.financeapp.Accounts;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kish.financeapp.Accounts.dtos.AccountResponseDto;
import com.kish.financeapp.Accounts.dtos.CreateAccountRequestDto;
import com.kish.financeapp.Accounts.enums.AccountStatus;
import com.kish.financeapp.Accounts.exceptions.AccountNotFoundException;
import com.kish.financeapp.Accounts.exceptions.DuplicateAccountException;
import com.kish.financeapp.Accounts.exceptions.IncorrectAccountBalanceException;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    public AccountService(AccountRepository repository) {
        this.accountRepository = repository;
    } 

    @Transactional
    public AccountResponseDto createAccount(@org.jetbrains.annotations.NotNull CreateAccountRequestDto accountRequest) {
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
        return mapToAccountResponse(saved);

    }

    public List<AccountResponseDto> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();

         return accounts.stream().map(this::mapToAccountResponse).toList();
    }

    

    @Transactional
    public AccountResponseDto markAccountClosed(int id){
         Account account = accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException("Account with ID: " + id + " not found."));

        if (account.getAvailableBalance().compareTo(BigDecimal.ZERO) != 0){
            throw new IncorrectAccountBalanceException("Account balance is " + account.getAvailableBalance() + ". Account balance must be zero, before deletion.");
        }

        account.setAccountStatus(AccountStatus.CLOSED);

        return mapToAccountResponse(account);
    }





    //helper functions
    private AccountResponseDto mapToAccountResponse(Account account){
        return new AccountResponseDto(
            account.getAccountID(),
            account.getName(),
            account.getAccountType(),
            account.getAvailableBalance().toString(),
            account.getAccountStatus()
        );
    }

}
