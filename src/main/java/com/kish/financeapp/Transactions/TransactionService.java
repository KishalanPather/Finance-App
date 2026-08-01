package com.kish.financeapp.Transactions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kish.financeapp.Accounts.Account;
import com.kish.financeapp.Accounts.AccountRepository;
import com.kish.financeapp.Accounts.exceptions.AccountNotFoundException;
import com.kish.financeapp.Envelopes.Envelope;
import com.kish.financeapp.Envelopes.EnvelopeRepository;
import com.kish.financeapp.Envelopes.exceptions.EnvelopeNotFoundException;
import com.kish.financeapp.Envelopes.exceptions.IncorrectEnvelopeBalanceException;
import com.kish.financeapp.Transactions.dtos.AddExpenseRequestDto;
import com.kish.financeapp.Transactions.dtos.AddIncomeRequestDto;
import com.kish.financeapp.Transactions.dtos.TransactionResponseDto;
import com.kish.financeapp.Transactions.enums.TransactionType;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final EnvelopeRepository envelopeRepository;

    public TransactionService(
        TransactionRepository transactionRepository,
        AccountRepository accountRepository,
        EnvelopeRepository envelopeRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.envelopeRepository = envelopeRepository;
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

    @Transactional
    public TransactionResponseDto addExpense(AddExpenseRequestDto expenseRequest){
        Envelope envelope = envelopeRepository.findById(expenseRequest.envelopeId())
            .orElseThrow(() -> new EnvelopeNotFoundException("Envelope with id: "+ expenseRequest.envelopeId() + " not found."));

        if (envelope.getEnvelopeBalance().compareTo(expenseRequest.amount()) < 0){
            throw new IncorrectEnvelopeBalanceException("Insufficient funds in Envelope.");
        }

        envelope.setEnvelopeBalance(
            envelope.getEnvelopeBalance().subtract(expenseRequest.amount())
        );

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








