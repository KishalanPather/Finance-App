package com.kish.financeapp.Accounts.dto;

import com.kish.financeapp.Accounts.enums.AccountType;

public record CreateAccountResponseDto(
     Integer accountID,
     String name,
     AccountType accountType,
     String availableBalance
) {}
