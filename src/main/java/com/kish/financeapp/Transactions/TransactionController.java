package com.kish.financeapp.Transactions;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kish.financeapp.Transactions.dtos.AddExpenseRequestDto;
import com.kish.financeapp.Transactions.dtos.AddIncomeRequestDto;
import com.kish.financeapp.Transactions.dtos.TransactionResponseDto;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    
    private final TransactionService transactionService;

    public TransactionController(TransactionService service){
        this.transactionService = service;
    }


    @PostMapping("/income")
    public TransactionResponseDto addIncome( @RequestBody AddIncomeRequestDto incomeRequest){
        return transactionService.addIncome(incomeRequest);
    }

    @PostMapping("/expense")
    public TransactionResponseDto addExpense(@RequestBody AddExpenseRequestDto expenseRequest){
        return transactionService.addExpense(expenseRequest);
    }

}
