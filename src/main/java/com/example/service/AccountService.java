package com.example.service;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidCredentialException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account newUser(Account user) throws DuplicateUsernameException, InvalidCredentialException{
        if(user.getUsername().length() > 0 && user.getPassword().length() >= 4){
            for(Account account:accountRepository.findAll()){
                if(account.getUsername().equals(user.getUsername())){
                    throw new DuplicateUsernameException(user.getUsername() + " is already in use.");
                }
            }
            return accountRepository.save(user);
        }
        throw new InvalidCredentialException("Invalid credentials!");
    }

    public Account loginUser(Account user) throws AuthenticationException{
        for(Account account:accountRepository.findAll()){
            if(account.getUsername().equals(user.getUsername()) && account.getPassword().equals(user.getPassword())){
                return user;
            }
        }
        throw new AuthenticationException("Check username and password credentials");
    }

    public Account userExists(int account_id){
        for(Account account:accountRepository.findAll()){
            if(account.getAccountId().equals(account_id)){
                return account;
            }
        }
        return null;
    }
}
