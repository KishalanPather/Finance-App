package com.kish.financeapp.Transactions.dtos;

import java.math.BigDecimal;

import com.kish.financeapp.Transactions.enums.TransactionType;

public record AddExpenseRequestDto(
    Integer envelopeId,
    BigDecimal amount,
    TransactionType transactionType,
    String description,
    String category,
    String note
){}
