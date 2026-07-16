package com.kish.financeapp.Transactions;

import org.springframework.stereotype.Service;

import com.kish.financeapp.Transactions.dtos.AddIncomeRequestDto;
import com.kish.financeapp.Transactions.dtos.TransactionResponseDto;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository repository){
        this.transactionRepository = repository;
    }

    public TransactionResponseDto addIncome(AddIncomeRequestDto incomeRequest){

    }
}