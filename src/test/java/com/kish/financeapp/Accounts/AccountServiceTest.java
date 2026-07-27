package com.kish.financeapp.Accounts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kish.financeapp.Accounts.dtos.AccountResponseDto;
import com.kish.financeapp.Accounts.dtos.CreateAccountRequestDto;
import com.kish.financeapp.Accounts.enums.AccountStatus;
import com.kish.financeapp.Accounts.enums.AccountType;
import com.kish.financeapp.Accounts.exceptions.AccountNotFoundException;
import com.kish.financeapp.Accounts.exceptions.IncorrectAccountBalanceException;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock 
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testCreateAccount() {
        //verify an account is saved to the database correctly

        CreateAccountRequestDto accountRequest = new CreateAccountRequestDto("Nedbank Account", AccountType.DEBIT);

        when(accountRepository.save(any(Account.class)))
            .thenAnswer(i -> i.getArgument(0));

        AccountResponseDto accountResponse = accountService.createAccount(accountRequest);

        assertEquals(accountResponse.accountType(), AccountType.DEBIT);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void testCreateAccountWithDuplicateName() {
        //verify that an exception is thrown when trying to create an account with a duplicate name

        CreateAccountRequestDto accountRequest = new CreateAccountRequestDto("Nedbank Account", AccountType.DEBIT);

        when(accountRepository.existsByName(accountRequest.name())).thenReturn(true);

        try {
            accountService.createAccount(accountRequest);
        } catch (Exception e) {
            assertEquals("Account with same name already exists.", e.getMessage());
        }
    }

// ------- View Account Tests
    

    @Test
    public void viewAllAccounts_return_all_accounts(){
        //When I call accountService.getAllAccounts(), it must return the correct response, which is a list of response dtos
        
        // arrange
        Account account1 = new Account(1, "Nedbank account",AccountType.DEBIT,BigDecimal.valueOf(0), AccountStatus.ACTIVE);
        Account account2 = new Account(2, "Discovery account",AccountType.CREDIT,BigDecimal.valueOf(200),AccountStatus.ACTIVE);
        Account account3 = new Account(3, "Capitec account",AccountType.DEBIT,BigDecimal.valueOf(400),AccountStatus.ACTIVE);

        when(accountRepository.findAll())
            .thenReturn(List.of(account1,account2,account3));

        // act
        List<AccountResponseDto> result = accountService.getAllAccounts();

        // assert
        assertEquals(3, result.size());
        assertEquals("Nedbank account", result.get(0).name());
        assertEquals("Discovery account", result.get(1).name());
        assertEquals("Capitec account", result.get(2).name());

        verify(accountRepository).findAll();

    }

    @Test
    public void  viewAllAccounts_return_empty_list(){
        // When I call accountService.getAllAccounts(), it must return an empty list
        when(accountRepository.findAll()).thenReturn(List.of());

        // act
        List<AccountResponseDto> result = accountService.getAllAccounts();

        // assert
        assertEquals(0, result.size());

        verify(accountRepository).findAll();
    }


    //------------------- Delete account tests
    @Test
    public void ShouldChangeStatusToClosed(){
        // Create an account,
        // when accountRepository is called, return that acc
        // run the function
        // assert the status

        //Arrange
        Account account = new Account(1, "Nedbank account",AccountType.DEBIT,BigDecimal.valueOf(0), AccountStatus.ACTIVE);

        when(accountRepository.findById(1))
            .thenReturn(Optional.of(account));

        //Act
        accountService.markAccountClosed(1);

        //Assert
        assertEquals(AccountStatus.CLOSED, account.getAccountStatus());


    }


    @Test
    public void ShouldThrowExceptionWhenAccountBalanceIsNotZero(){
        //Arrange account with balance != 0
        Account account = new Account(1, "Nedbank account",AccountType.DEBIT,BigDecimal.valueOf(322), AccountStatus.ACTIVE);

        when(accountRepository.findById(1))
            .thenReturn(Optional.of(account));

        
        //act and assert
        assertThrows(IncorrectAccountBalanceException.class, () -> accountService.markAccountClosed(1));
    }


    @Test
    public void ShouldThrowExceptionWhenAccountDoesNotExist(){
        //arrange
        when(accountRepository.findById(1))
            .thenReturn(Optional.empty());

        //act and assert
        assertThrows(AccountNotFoundException.class, () -> accountService.markAccountClosed(1));
    }


    @Test
    public void ShouldReturnCorrectResponseDto(){
         //Arrange
        Account account = new Account(1, "Nedbank account",AccountType.DEBIT,BigDecimal.valueOf(0), AccountStatus.ACTIVE);

        when(accountRepository.findById(1))
            .thenReturn(Optional.of(account));


        
        //act
        AccountResponseDto response = accountService.markAccountClosed(1);
        
        //assert
        assertEquals(account.getAccountID(), response.accountID());
        assertEquals(account.getName(), response.name());
        assertEquals(account.getAccountType(), response.accountType());
        assertEquals(account.getAvailableBalance().toString(), response.availableBalance());
        assertEquals(account.getAccountStatus(), response.accountStatus());

    }
    
}