package com.kish.financeapp.Transactions;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kish.financeapp.Accounts.Account;
import com.kish.financeapp.Accounts.AccountRepository;
import com.kish.financeapp.Transactions.dtos.AddIncomeRequestDto;
import com.kish.financeapp.Transactions.dtos.TransactionResponseDto;
import com.kish.financeapp.Transactions.exceptions.AccountNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(
        TransactionRepository transactionRepository,
        AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionResponseDto addIncome(AddIncomeRequestDto incomeRequest){
        //validate to find account, fail fast
        Account account = accountRepository.findById(incomeRequest.accountId())
            .orElseThrow(() -> new AccountNotFoundException("Account with ID: " + incomeRequest.accountId() + " not found."));
            return new TransactionResponseDto();
    }
}








