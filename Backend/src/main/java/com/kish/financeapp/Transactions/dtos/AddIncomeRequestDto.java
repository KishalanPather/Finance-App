package com.kish.financeapp.Transactions.dtos;

import java.math.BigDecimal;

import com.kish.financeapp.Transactions.enums.TransactionType;

public record AddIncomeRequestDto(
    Integer accountId,
    BigDecimal amount,
    TransactionType transactionType,
    String description,
    String category,
    String note
) {}
