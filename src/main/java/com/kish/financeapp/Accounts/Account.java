package com.kish.financeapp.Accounts;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.kish.financeapp.Accounts.enums.AccountType;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter 
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountID;

    private String name;

    @Enumerated(EnumType.STRING)
    private AccountType accountType; 

    private BigDecimal availableBalance;

}
