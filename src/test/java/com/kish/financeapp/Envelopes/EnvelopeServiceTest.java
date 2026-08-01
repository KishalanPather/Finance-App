package com.kish.financeapp.Envelopes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kish.financeapp.Accounts.Account;
import com.kish.financeapp.Accounts.AccountRepository;
import com.kish.financeapp.Accounts.enums.AccountStatus;
import com.kish.financeapp.Accounts.exceptions.AccountNotFoundException;
import com.kish.financeapp.Accounts.exceptions.IncorrectAccountBalanceException;
import com.kish.financeapp.Accounts.exceptions.IncorrectAccountStateException;
import com.kish.financeapp.Envelopes.dtos.CreateEnvelopeRequestDto;
import com.kish.financeapp.Envelopes.dtos.EnvelopeResponseDto;
import com.kish.financeapp.Envelopes.dtos.FundRequestDto;
import com.kish.financeapp.Envelopes.enums.EnvelopeGroup;
import com.kish.financeapp.Envelopes.exceptions.DuplicateEnvelopeException;
import com.kish.financeapp.Envelopes.exceptions.EnvelopeNotFoundException;
import com.kish.financeapp.Transactions.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class EnvelopeServiceTest {
    
    @Mock
    EnvelopeRepository envelopeRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;


    @InjectMocks 
    EnvelopeService envelopeService;

// --- Create Envelope Tests
    @Test
    public void shouldSaveEnvelopeWithZeroBalance(){
        CreateEnvelopeRequestDto envelopeRequest = new CreateEnvelopeRequestDto(
            "Test Envelope",
            EnvelopeGroup.FIXED,
            BigDecimal.valueOf(500)
        );

         when(envelopeRepository.save(any(Envelope.class)))
            .thenAnswer(i -> i.getArgument(0));

        EnvelopeResponseDto envelopeResponse = envelopeService.createEnvelope(envelopeRequest);
        assertEquals(BigDecimal.ZERO, envelopeResponse.envelopeBalance());
        verify(envelopeRepository).save(any(Envelope.class));

    }

    @Test
    public void ShouldThrowExceptionWhenEnvelopeNameExists(){
        CreateEnvelopeRequestDto envelopeRequest = new CreateEnvelopeRequestDto(
            "Test Envelope",
            EnvelopeGroup.FIXED,
            BigDecimal.valueOf(500)
        );

        when(envelopeRepository.existsByName(envelopeRequest.name()))
            .thenReturn(true);

        
        assertThrows(DuplicateEnvelopeException.class, () -> envelopeService.createEnvelope(envelopeRequest));
    }

    @Test
    public void ShouldReturnCorrectEnvelopeResponseDto(){
        CreateEnvelopeRequestDto envelopeRequest = new CreateEnvelopeRequestDto(
            "Test Envelope",
            EnvelopeGroup.FIXED,
            BigDecimal.valueOf(500)
        );

        Envelope envelope = Envelope.builder()
            .envelopeId(1)
            .name(envelopeRequest.name())
            .envelopeGroup(envelopeRequest.envelopeGroup())
            .envelopeLimit(envelopeRequest.envelopeLimit())
            .envelopeBalance(BigDecimal.ZERO)
            .build();

        when(envelopeRepository.save(any(Envelope.class)))
            .thenReturn(envelope);

        EnvelopeResponseDto envelopeResponse = envelopeService.createEnvelope(envelopeRequest);

        assertEquals(envelope.getEnvelopeId(), envelopeResponse.envelopeId());
        assertEquals(envelope.getName(), envelopeResponse.name());
        assertEquals(envelope.getEnvelopeGroup(), envelopeResponse.envelopeGroup());
        assertEquals(envelope.getEnvelopeLimit(), envelopeResponse.envelopeLimit());
        assertEquals(envelope.getEnvelopeBalance(), envelopeResponse.envelopeBalance());

        
    }

    // ---- Fund Envelope Tests
    @Test
    public void ShouldIncreaseEnvelopeBalanceAndDecreaseAccountBalance(){

        FundRequestDto fundRequest = new FundRequestDto(1, BigDecimal.valueOf(400));


        //Create mock account
        Account account = new Account();
        account.setAccountID(1);
        account.setAvailableBalance(BigDecimal.valueOf(1000));

        //Create mock envelope
        Envelope envelope = new Envelope();
        envelope.setEnvelopeId(1);
        envelope.setEnvelopeBalance(BigDecimal.ZERO);

        //Define behavior for mocks
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(envelopeRepository.findById(1)).thenReturn(Optional.of(envelope));
        when(transactionRepository.save(any())).thenReturn(null); // Mock transaction save

        //Call the service method
        envelopeService.fundEnvelope(1,fundRequest);

        //Verify interactions and state
        assertEquals(BigDecimal.valueOf(400), envelope.getEnvelopeBalance());
        assertEquals(BigDecimal.valueOf(600), account.getAvailableBalance());
    }

    @Test
    public void ShouldThrowExceptionIfAccountNotFound(){
        FundRequestDto fundRequest = new FundRequestDto(1, BigDecimal.valueOf(400));

        when(accountRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> envelopeService.fundEnvelope(1, fundRequest));
    }

    @Test
    public void ShouldThrowExceptionIfAccountInClosedState(){
        FundRequestDto fundRequest = new FundRequestDto(1, BigDecimal.valueOf(400));

        Account account = new Account();
        account.setAccountID(1);
        account.setAvailableBalance(BigDecimal.valueOf(1000));
        account.setAccountStatus(AccountStatus.CLOSED);

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        assertThrows(IncorrectAccountStateException.class, () -> envelopeService.fundEnvelope(1, fundRequest));
    }

    @Test
    public void ShouldThrowExceptionIfAmountGreaterThanAccountBalance(){
        FundRequestDto fundRequest = new FundRequestDto(1, BigDecimal.valueOf(1200));

        Account account = new Account();
        account.setAccountID(1);
        account.setAvailableBalance(BigDecimal.valueOf(1000));
        account.setAccountStatus(AccountStatus.ACTIVE);

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        assertThrows(IncorrectAccountBalanceException.class, () -> envelopeService.fundEnvelope(1, fundRequest));
    }

    @Test
    public void ShouldThrowExceptionIfEnvelopeNotFound(){
        FundRequestDto fundRequest = new FundRequestDto(1, BigDecimal.valueOf(400));

        Account account = new Account();
        account.setAccountID(1);
        account.setAvailableBalance(BigDecimal.valueOf(1000));
        account.setAccountStatus(AccountStatus.ACTIVE);

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));


        when(envelopeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EnvelopeNotFoundException.class, () -> envelopeService.fundEnvelope(1, fundRequest));
    }

    @Test
    public void ShouldSaveTransaction(){
        FundRequestDto fundRequest = new FundRequestDto(1, BigDecimal.valueOf(400));

        Account account = new Account();
        account.setAccountID(1);
        account.setAvailableBalance(BigDecimal.valueOf(1000));
        account.setAccountStatus(AccountStatus.ACTIVE);

        Envelope envelope = new Envelope();
        envelope.setEnvelopeId(1);
        envelope.setEnvelopeBalance(BigDecimal.ZERO);

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(envelopeRepository.findById(1)).thenReturn(Optional.of(envelope));
        when(transactionRepository.save(any())).thenReturn(null); // Mock transaction save

        envelopeService.fundEnvelope(1, fundRequest);

        verify(transactionRepository).save(any());
    }

    @Test
    public void ShouldReturnEnvelopeResponseDto(){
        FundRequestDto fundRequest = new FundRequestDto(1, BigDecimal.valueOf(400));

        Account account = new Account();
        account.setAccountID(1);
        account.setAvailableBalance(BigDecimal.valueOf(1000));
        account.setAccountStatus(AccountStatus.ACTIVE);

        Envelope envelope = new Envelope();
        envelope.setEnvelopeId(1);
        envelope.setName("Test");
        envelope.setEnvelopeGroup(EnvelopeGroup.FIXED);
        envelope.setEnvelopeLimit(BigDecimal.valueOf(10000));
        envelope.setEnvelopeBalance(BigDecimal.ZERO);


        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(envelopeRepository.findById(1)).thenReturn(Optional.of(envelope));
        when(transactionRepository.save(any())).thenReturn(null); // Mock transaction save

        EnvelopeResponseDto envelopeResponse = envelopeService.fundEnvelope(1, fundRequest);

        assertEquals(envelope.getEnvelopeId(), envelopeResponse.envelopeId());
        assertEquals(envelope.getName(), envelopeResponse.name());
        assertEquals(envelope.getEnvelopeGroup(), envelopeResponse.envelopeGroup());
        assertEquals(envelope.getEnvelopeLimit(), envelopeResponse.envelopeLimit());
        assertEquals(envelope.getEnvelopeBalance(), envelopeResponse.envelopeBalance());
    }

}   
