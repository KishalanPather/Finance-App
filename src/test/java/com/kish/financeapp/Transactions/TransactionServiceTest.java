package com.kish.financeapp.Transactions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.kish.financeapp.Accounts.exceptions.DuplicateAccountException;
import com.kish.financeapp.Transactions.dtos.AddIncomeRequestDto;
import com.kish.financeapp.Transactions.dtos.TransactionResponseDto;
import com.kish.financeapp.Transactions.enums.TransactionType;
import com.kish.financeapp.Transactions.exceptions.AccountNotFoundException;

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
        //Arrange: Build dto
        AddIncomeRequestDto incomeRequest = new AddIncomeRequestDto(
            1,
            BigDecimal.valueOf(100),
            TransactionType.INCOME,
            "Salary",
            "Job",
            "Monthly salary"
        );

        //build what the database returns, if not here, code will throw an error as unable to save a transaction.
        Transaction saved = new Transaction();  //empty bc content not important for this test

        //build an account so getAccountById returns something
        Account account = new Account();
        account.setAvailableBalance(BigDecimal.valueOf(400));   //only concerned with the balance field 
        
        //mock
        when(accountRepository.findById(1))
            .thenReturn(Optional.of(account));
        

        when(transactionRepository.save(any(Transaction.class)))
            .thenReturn(saved);

        
        //act
        transactionService.addIncome(incomeRequest);

        //assert: ensure the transaction was saved
        verify(transactionRepository).save(any(Transaction.class));

        
    }

    @Test
    public void shouldIncreaseAccountBalance(){
        // will need to arrange an income dto, and account with a balance.
        // mock the findbyId account repository method and transaction.
        // assert account availableBalance 


        //arrange
        AddIncomeRequestDto incomeRequest = new AddIncomeRequestDto(
            1,
            BigDecimal.valueOf(100),
            TransactionType.INCOME,
            "Salary",
            "Job",
            "Monthly salary"
        );
        
        Account account = new Account();
        account.setAvailableBalance(BigDecimal.valueOf(400));   //only concerned about the balance in this test.

        Transaction transaction = new Transaction();            //empty, bc content not important for this test.

        //mock
        when(accountRepository.findById(1))
            .thenReturn(Optional.of(account));

         when(transactionRepository.save(any(Transaction.class)))
        .thenReturn(transaction);

        //act
        transactionService.addIncome(incomeRequest);

        //assert
        assertEquals(BigDecimal.valueOf(500), account.getAvailableBalance());



    }

    @Test
    public void shouldReturnTransactionResponseDto(){

        //arrange
        AddIncomeRequestDto incomeRequest = new AddIncomeRequestDto(
            1,
            BigDecimal.valueOf(100),
            TransactionType.INCOME,
            "Salary",
            "Job",
            "Monthly salary"
        );

        Account account = new Account();
        account.setAvailableBalance(BigDecimal.valueOf(400)); //only balance needed for this test

        Transaction transaction = Transaction.builder()
            .transactionId(5)
            .envelopeId(null)
                .accountId(incomeRequest.accountId())
                .amount(incomeRequest.amount())
                .description(incomeRequest.description())
                .transactionType(incomeRequest.transactionType())
                .category(incomeRequest.category())
                .date(new Date())
                .note(incomeRequest.note())
                .build();



        //mock
        when(accountRepository.findById(1))
            .thenReturn(Optional.of(account));

        when(transactionRepository.save(any(Transaction.class)))
        .thenReturn(transaction);

        //act
         TransactionResponseDto response = transactionService.addIncome(incomeRequest);

         //assert
         assertEquals(transaction.getTransactionId(), response.transactionId());
         assertEquals(transaction.getEnvelopeId(), response.envelopeId());
         assertEquals(transaction.getAccountId(), response.accountId());
         assertEquals(transaction.getAmount(), response.amount());
         assertEquals(transaction.getDescription(),response.description());
         assertEquals(transaction.getCategory(),response.category());
         assertEquals(transaction.getNote(),response.note());

    }

    @Test
    public void shouldThrowExceptionWhenAccountDoesNotExist(){
         //arrange
        AddIncomeRequestDto incomeRequest = new AddIncomeRequestDto(
            1,
            BigDecimal.valueOf(100),
            TransactionType.INCOME,
            "Salary",
            "Job",
            "Monthly salary"
        );

        //mock
        when(accountRepository.findById(1))
            .thenReturn(Optional.empty());


        //assert and act
        assertThrows(AccountNotFoundException.class, () -> transactionService.addIncome(incomeRequest));

    }

    @Test
    public void shouldThrowExceptionWhenAmountIsNegative(){}
}
