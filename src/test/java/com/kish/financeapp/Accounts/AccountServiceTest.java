package com.kish.financeapp.Accounts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kish.financeapp.Accounts.dto.CreateAccountRequestDto;
import com.kish.financeapp.Accounts.dto.CreateAccountResponseDto;
import com.kish.financeapp.Accounts.enums.AccountType;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock 
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testCreateAccount() {
        //When I give the service a name and account type, it returns an id, name, account type and available balance

        //create an account req
        CreateAccountRequestDto accountRequest = new CreateAccountRequestDto("Nedbank Account", AccountType.DEBIT);

        when(accountRepository.save(any(Account.class)))
            .thenAnswer(i -> i.getArgument(0));

        CreateAccountResponseDto accountResponse = accountService.createAccount(accountRequest);

        assertEquals(accountResponse.accountType(), AccountType.DEBIT);
       
        //assert
        verify(accountRepository).save(any(Account.class));
    }

    

    

    
    
}