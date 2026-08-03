package com.kish.financeapp.Transactions.dtos;

import java.math.BigDecimal;
import java.util.Date;

import com.kish.financeapp.Transactions.enums.TransactionType;

public record TransactionResponseDto(
    Integer transactionId,
    Integer envelopeId,
    Integer accountId,
    BigDecimal amount,
    String description,
    String category,
    String note,
    TransactionType transactionType,
    Date date
) {
}
