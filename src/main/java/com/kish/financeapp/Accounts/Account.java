package com.kish.financeapp.Accounts;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter 
@Setter
@Entity
public class Account {
    private Integer accountID;
    private String name;
    private String accountType; //Change to an enum eventually
    private long availableBalance;

}
