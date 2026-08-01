package com.kish.financeapp.Transactions;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.kish.financeapp.Accounts.Account;
import com.kish.financeapp.Accounts.AccountRepository;
import com.kish.financeapp.Accounts.exceptions.AccountNotFoundException;
import com.kish.financeapp.Transactions.dtos.AddIncomeRequestDto;
import com.kish.financeapp.Transactions.dtos.TransactionResponseDto;

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
        Account account = getAccountById(incomeRequest.accountId());

        account.setAvailableBalance(
            account.getAvailableBalance().add(incomeRequest.amount())
            );

            Transaction transaction = createIncomeTransaction(incomeRequest);

            Transaction saved = transactionRepository.save(transaction);

            return mapToResponse(saved);

        
    }

    //Helper Functions
    private Account getAccountById(Integer accountId){
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account with ID: " + accountId + " not found."));
    }

    private Transaction createIncomeTransaction(AddIncomeRequestDto incomeRequest){
        return Transaction.builder()
                .envelopeId(null)
                .accountId(incomeRequest.accountId())
                .amount(incomeRequest.amount())
                .description(incomeRequest.description())
                .transactionType(incomeRequest.transactionType())
                .category(incomeRequest.category())
                .date(new Date())
                .note(incomeRequest.note())
                .build();
    }

    private TransactionResponseDto mapToResponse(Transaction transaction){
        return new TransactionResponseDto(
                transaction.getTransactionId(),
                transaction.getEnvelopeId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getCategory(),
                transaction.getNote(),
                transaction.getTransactionType(),
                transaction.getDate()
            );
    }
}








