package com.kish.financeapp.Transactions.dtos;

import java.math.BigDecimal;

public record AddIncomeRequestDto(
    Integer accountId,
    BigDecimal amount,
    String description,
    String category,
    String note
) {}
