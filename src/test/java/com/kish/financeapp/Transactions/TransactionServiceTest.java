package com.kish.financeapp.Transactions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kish.financeapp.Accounts.Account;
import com.kish.financeapp.Accounts.AccountRepository;
import com.kish.financeapp.Accounts.exceptions.AccountNotFoundException;
import com.kish.financeapp.Envelopes.Envelope;
import com.kish.financeapp.Envelopes.EnvelopeRepository;
import com.kish.financeapp.Transactions.dtos.AddExpenseRequestDto;
import com.kish.financeapp.Transactions.dtos.AddIncomeRequestDto;
import com.kish.financeapp.Transactions.dtos.TransactionResponseDto;
import com.kish.financeapp.Transactions.enums.TransactionType;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EnvelopeRepository envelopeRepository;

    @InjectMocks
    private TransactionService transactionService;


    //----------- Add an Income tests

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

    // -- Add an Expense transaction
    
    @Test
    public void shouldSaveExpenseTransaction(){
        //arrange
        AddExpenseRequestDto expenseRequest = new AddExpenseRequestDto(
            1,
            BigDecimal.valueOf(50),
            TransactionType.EXPENSE,
            "Groceries",
            "Food",
            "Weekly groceries"
        );

        Envelope envelope = new Envelope();
        envelope.setEnvelopeBalance(BigDecimal.valueOf(400)); //only balance needed for this test

        //mock
        when(envelopeRepository.findById(1))
            .thenReturn(Optional.of(envelope));


        //act
        transactionService.addExpense(expenseRequest);

        //assert
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void shouldDecreaseEnvelopeBalance(){
        //arrange
        AddExpenseRequestDto expenseRequest = new AddExpenseRequestDto(
            1,
            BigDecimal.valueOf(50),
            TransactionType.EXPENSE,
            "Groceries",
            "Food",
            "Weekly groceries"
        );

        Envelope envelope = new Envelope();
        envelope.setEnvelopeBalance(BigDecimal.valueOf(400)); //only balance needed for this test

        //mock
        when(envelopeRepository.findById(1))
            .thenReturn(Optional.of(envelope));


        //act
        transactionService.addExpense(expenseRequest);

        //assert
        assertEquals(BigDecimal.valueOf(350), envelope.getEnvelopeBalance());
    }

    @Test
    public void shouldReturnCorrectTransactionResponseForExpenseDto(){}

    @Test
    public void shouldThrowExceptionWhenEnvelopeNotFound(){}


    @Test
    public void shouldthrowExceptionWhenInsufficientEnvelopeBalance(){}





    // --- View All Transactions Tests
    @Test
    public void shouldReturnAllTransactions(){
        //arrange
        Transaction transaction1 = new Transaction(1, null, 1, BigDecimal.valueOf(100), "Salary", TransactionType.INCOME, "Job", new Date(), "Monthly salary");
        Transaction transaction2 = new Transaction(2, null, 1, BigDecimal.valueOf(50), "Groceries", TransactionType.EXPENSE, "Food", new Date(), "Weekly groceries");
        Transaction transaction3 = new Transaction(3, null, 1, BigDecimal.valueOf(200), "Freelance", TransactionType.INCOME, "Job", new Date(), "Freelance project");

        when(transactionRepository.findAll()).thenReturn(List.of(transaction1, transaction2, transaction3));

        //act
        List<TransactionResponseDto> response = transactionService.getAllTransactions();

        //assert
        assertEquals(3, response.size());
        assertEquals(1, response.get(0).transactionId());
        assertEquals(2, response.get(1).transactionId());
        assertEquals(3, response.get(2).transactionId());
        }

        @Test
        public void shouldReturnEmptyListWhenNoTransactions(){
            //arrange
            when(transactionRepository.findAll()).thenReturn(List.of());

            //act
            List<TransactionResponseDto> response = transactionService.getAllTransactions();

            //assert
            assertEquals(0, response.size());

            verify(transactionRepository).findAll();

        }
    }
