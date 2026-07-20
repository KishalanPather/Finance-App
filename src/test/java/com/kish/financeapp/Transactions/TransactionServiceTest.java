package com.kish.financeapp.Transactions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.foreign.Linker.Option;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kish.financeapp.Accounts.Account;
import com.kish.financeapp.Accounts.AccountRepository;
import com.kish.financeapp.Accounts.enums.AccountType;
import com.kish.financeapp.Transactions.dtos.AddIncomeRequestDto;
import com.kish.financeapp.Transactions.enums.TransactionType;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    public void shouldSaveIncomeTransaction(){
        AddIncomeRequestDto incomeRequest = new AddIncomeRequestDto(
            1,
            BigDecimal.valueOf(100),
            TransactionType.INCOME,
            "Salary",
            "Job",
            "Monthly salary"
        );

        Transaction saved = Transaction.builder()
                .envelopeId(null)
                .accountId(incomeRequest.accountId())
                .amount(incomeRequest.amount())
                .description(incomeRequest.description())
                .transactionType(incomeRequest.transactionType())
                .category(incomeRequest.category())
                .date(new Date())
                .note(incomeRequest.note())
                .build();


        Account account = new Account(
            1,
            "Sample Account",
            AccountType.DEBIT,
            BigDecimal.valueOf(400)
        );
        
        when(accountRepository.findById(1))
            .thenReturn(Optional.of(account));
        

        when(transactionRepository.save(any(Transaction.class)))
            .thenReturn(saved);

        transactionService.addIncome(incomeRequest);

        verify(transactionRepository).save(any(Transaction.class));

        
    }

    @Test
    public void shouldIncreaseAccountBalance(){}

    @Test
    public void shouldReturnTransactionResponseDto(){}

    @Test
    public void shouldThrowExceptionWhenAmountIsNegative(){}

    @Test
    public void shouldThrowExceptionWhenAccountDoesNotExist(){}
}
