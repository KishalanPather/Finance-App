package com.kish.financeapp.Accounts.dtos;

import com.kish.financeapp.Accounts.enums.AccountType;

public record CreateAccountRequestDto(
    String name,
    AccountType accountType) {}   
    