package com.kish.financeapp.Accounts.dto;

import com.kish.financeapp.Accounts.enums.AccountType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAccountRequestDto {
    private String name;
    private AccountType accountType;
}   
