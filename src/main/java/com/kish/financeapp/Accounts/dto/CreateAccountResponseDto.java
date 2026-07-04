package com.kish.financeapp.Accounts.dto;

import com.kish.financeapp.Accounts.Account;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateAccountResponseDto {
    private Integer accountID;
    private String name;
    private String accountType;
    private String availableBalance;
}
