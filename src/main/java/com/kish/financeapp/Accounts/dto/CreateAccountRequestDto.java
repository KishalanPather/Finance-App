package com.kish.financeapp.Accounts.dto;

import com.kish.financeapp.Accounts.enums.AccountType;

public record CreateAccountRequestDto(
    String name,
    AccountType accountType) {}   
