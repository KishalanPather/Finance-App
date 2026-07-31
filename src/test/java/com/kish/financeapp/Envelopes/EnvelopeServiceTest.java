package com.kish.financeapp.Envelopes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kish.financeapp.Accounts.Account;
import com.kish.financeapp.Accounts.exceptions.IncorrectAccountBalanceException;
import com.kish.financeapp.Envelopes.dtos.CreateEnvelopeRequestDto;
import com.kish.financeapp.Envelopes.dtos.EnvelopeResponseDto;
import com.kish.financeapp.Envelopes.enums.EnvelopeGroup;
import com.kish.financeapp.Envelopes.exceptions.DuplicateEnvelopeException;

@ExtendWith(MockitoExtension.class)
public class EnvelopeServiceTest {
    
    @Mock
    EnvelopeRepository envelopeRepository;

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
        
    }

    @Test
    public void ShouldThrowExceptionIfAccountNotFound(){
        // Test implementation
    }

    @Test
    public void ShouldThrowExceptionIfAccountInClosedState(){
        // Test implementation
    }

    @Test
    public void ShouldThrowExceptionIfAmountGreaterThanAccountBalance(){
        // Test implementation
    }

    @Test
    public void ShouldThrowExceptionIfEnvelopeNotFound(){
        // Test implementation
    }

    @Test
    public void ShouldSaveAccountBalanceAndEnvelopeBalanceAndTransaction(){
        // Test implementation
    }

    @Test
    public void ShouldReturnTransferResponseDto(){
        // Test implementation
    }

}
