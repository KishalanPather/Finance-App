package com.kish.financeapp.Transactions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    public void shouldSaveIncomeTransaction(){
        AddIncomeRequestDto request = new AddIncomeRequestDto();

        Transaction saved = new Transaction();


        when(transactionRepository.save(any(Transaction.class)))
            .thenReturn(saved);

        transactionService.addIncome(request);

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
