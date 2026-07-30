package com.kish.financeapp.Envelopes;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.kish.financeapp.Accounts.Account;
import com.kish.financeapp.Accounts.AccountRepository;
import com.kish.financeapp.Accounts.enums.AccountStatus;
import com.kish.financeapp.Accounts.exceptions.AccountNotFoundException;
import com.kish.financeapp.Accounts.exceptions.IncorrectAccountBalanceException;
import com.kish.financeapp.Accounts.exceptions.IncorrectAccountStateException;
import com.kish.financeapp.Envelopes.dtos.CreateEnvelopeRequestDto;
import com.kish.financeapp.Envelopes.dtos.EnvelopeResponseDto;
import com.kish.financeapp.Envelopes.dtos.FundRequestDto;
import com.kish.financeapp.Envelopes.exceptions.DuplicateEnvelopeException;
import com.kish.financeapp.Envelopes.exceptions.EnvelopeNotFoundException;
import com.kish.financeapp.Transactions.Transaction;
import com.kish.financeapp.Transactions.TransactionRepository;
import com.kish.financeapp.Transactions.enums.TransactionType;

import jakarta.transaction.Transactional;

@Service
public class EnvelopeService {
    private final EnvelopeRepository envelopeRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public EnvelopeService(
        EnvelopeRepository envelopeRepository,
        TransactionRepository transactionRepository,
        AccountRepository accountRepository) {
        this.envelopeRepository = envelopeRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public EnvelopeResponseDto createEnvelope(CreateEnvelopeRequestDto envelopeRequest){
        if (envelopeRepository.existsByName(envelopeRequest.name())){
            throw new DuplicateEnvelopeException("Envelope with same name already exists.");
        }

        Envelope envelope = createEnvelopeObject(envelopeRequest);

        Envelope saved = envelopeRepository.save(envelope);

        EnvelopeResponseDto envelopeResponse = mapEnvelopeResponse(saved);

        return envelopeResponse;
    }


    @Transactional
    public EnvelopeResponseDto fundEnvelope(Integer envelopeId, FundRequestDto fundRequest){
        Account account = accountRepository.findById(fundRequest.accountId())
            .orElseThrow(() -> new AccountNotFoundException("Account with ID: " + fundRequest.accountId() + " not found."));

        if (account.getAccountStatus() == AccountStatus.CLOSED ){
            throw new IncorrectAccountStateException("Account cannot be closed.");
        }

        BigDecimal requestedAmount = fundRequest.amount();
        BigDecimal accountBalance = account.getAvailableBalance();

        if (requestedAmount.compareTo(accountBalance) > 0){
            throw new IncorrectAccountBalanceException("Insufficient funds in account.");
        }

        Envelope envelope = envelopeRepository.findById(envelopeId)
            .orElseThrow(() -> new EnvelopeNotFoundException("Envelope with ID: "+ envelopeId + " not found." ));
        
        account.setAvailableBalance(account.getAvailableBalance().subtract(fundRequest.amount()));
        envelope.setEnvelopeBalance(envelope.getEnvelopeBalance().add(fundRequest.amount()));


        Transaction transaction = Transaction.builder()
            .envelopeId(envelopeId)
            .accountId(fundRequest.accountId())
            .amount(fundRequest.amount())
            .description("Fund Envelope")
            .transactionType(TransactionType.TRANSFER)
            .category("Fund Envelope")
            .date(new Date())
            .note(null)
            .build();

        transactionRepository.save(transaction);

        EnvelopeResponseDto envelopeResponse = mapEnvelopeResponse(envelope);
        return envelopeResponse;
    }


// ----------- Helper functions
    private Envelope createEnvelopeObject(CreateEnvelopeRequestDto envelopeRequest){
        return Envelope.builder()
            .envelopeId(null)
            .name(envelopeRequest.name())
            .envelopeGroup(envelopeRequest.envelopeGroup())
            .envelopeLimit(envelopeRequest.envelopeLimit())
            .envelopeBalance(BigDecimal.ZERO)
            .build();
    }

    private EnvelopeResponseDto mapEnvelopeResponse(Envelope envelope){
        return new EnvelopeResponseDto(
            envelope.getEnvelopeId(),
            envelope.getName(),
            envelope.getEnvelopeGroup(),
            envelope.getEnvelopeLimit(),
            envelope.getEnvelopeBalance()
        );
    }

}
