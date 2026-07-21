package com.kish.financeapp.Transactions;

import java.math.BigDecimal;
import java.util.Date;

import com.kish.financeapp.Transactions.enums.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter 
@Setter
@Builder
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    private Integer envelopeId;

    private Integer accountId;

    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    
    private String category;

    private Date date;
    
    private String note;
    

}
