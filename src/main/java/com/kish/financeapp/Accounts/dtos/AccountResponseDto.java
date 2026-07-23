package com.kish.financeapp.Accounts.dtos;

import com.kish.financeapp.Accounts.enums.AccountStatus;
import com.kish.financeapp.Accounts.enums.AccountType;

public record AccountResponseDto(
     Integer accountID,
     String name,
     AccountType accountType,
     String availableBalance,
     AccountStatus accountStatus
) {}
