package com.kish.financeapp.Transactions.dtos;

import java.math.BigDecimal;

public record AddExpenseRequestDto(
    Integer envelopeId,
    BigDecimal amount,
    String description,
    String category,
    String note
){}
