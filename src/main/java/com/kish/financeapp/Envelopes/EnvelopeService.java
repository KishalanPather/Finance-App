package com.kish.financeapp.Envelopes;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.kish.financeapp.Accounts.AccountRepository;
import com.kish.financeapp.Envelopes.dtos.CreateEnvelopeRequestDto;
import com.kish.financeapp.Envelopes.dtos.EnvelopeResponseDto;
import com.kish.financeapp.Envelopes.exceptions.DuplicateEnvelopeException;
import com.kish.financeapp.Transactions.TransactionRepository;

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

    public EnvelopeResponseDto createEnvelope(CreateEnvelopeRequestDto envelopeRequest){
        if (envelopeRepository.existsByName(envelopeRequest.name())){
            throw new DuplicateEnvelopeException("Envelope with same name already exists.");
        }

        Envelope envelope = createEnvelopeObject(envelopeRequest);

        Envelope saved = envelopeRepository.save(envelope);

        EnvelopeResponseDto envelopeResponse = mapEnvelopeResponse(saved);

        return envelopeResponse;
    }


    public EnvelopeResponseDto fundEnvelope(Integer id, FundRequestDto incomeRequest){
        
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
