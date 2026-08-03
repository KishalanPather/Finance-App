package com.kish.financeapp.Envelopes.dtos;

import java.math.BigDecimal;

import com.kish.financeapp.Envelopes.enums.EnvelopeGroup;

public record CreateEnvelopeRequestDto(
    String name,
    EnvelopeGroup envelopeGroup,
    BigDecimal envelopeLimit
) {}
