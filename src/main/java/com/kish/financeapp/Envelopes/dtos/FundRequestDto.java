package com.kish.financeapp.Envelopes.dtos;

import java.math.BigDecimal;

public record FundRequestDto(
    Integer accountId,
    BigDecimal amount
) {}
