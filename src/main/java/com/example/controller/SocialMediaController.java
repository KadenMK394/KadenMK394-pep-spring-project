package com.example.controller;

import java.util.List;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidCredentialException;
import com.example.exception.InvalidMessageException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> newUser(@RequestBody Account user) throws DuplicateUsernameException, InvalidCredentialException{
        accountService.newUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateUsername(DuplicateUsernameException ex){
        return ex.getMessage();        
    }

    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidCredentials(InvalidCredentialException ex){
        return ex.getMessage();
    }

    @PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account user) throws AuthenticationException{
        Account loggedUser = accountService.loginUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(loggedUser);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedUser(AuthenticationException ex){
        return ex.getMessage();
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> newMessage(@RequestBody Message message) throws InvalidMessageException{
        messageService.newMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @ExceptionHandler(InvalidMessageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidMessage(InvalidMessageException ex){
        return ex.getMessage();
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> allMessages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(allMessages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> findMessage(@PathVariable int messageId){
        Message message = messageService.findMessage(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int messageId){
        Integer rows = messageService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(rows);
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@RequestBody Message message, @PathVariable int messageId) throws InvalidMessageException{
        messageService.updateMessage(message, messageId);
        return ResponseEntity.status(HttpStatus.OK).body(1);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllUserMessages(@PathVariable int accountId){
        List<Message> allUserMessages = messageService.getAllUserMessages(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(allUserMessages);
    }
}
