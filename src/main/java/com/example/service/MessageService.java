package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidMessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    
    MessageRepository messageRepository;
    AccountRepository accountRepository;
    AccountService accountService;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository, AccountService accountService){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public Message newMessage(Message message) throws InvalidMessageException{
        int length = message.getMessageText().length();
        if(length > 0 && length <= 255 && accountService.userExists(message.getPostedBy()) != null){
            return messageRepository.save(message);
        }
        throw new InvalidMessageException("Invalid message!");
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message findMessage(int message_id){
        for(Message message:messageRepository.findAll()){
            if(message.getMessageId().equals(message_id)) {
                return message;
            }
        }
        return null;
    }

    public Integer deleteMessage(int message_id){
        Message delMessage = findMessage(message_id);
        if(delMessage != null) {
            messageRepository.delete(delMessage);
            return 1;
        }
        return null;
    }

    public Message updateMessage(Message message, int message_id) throws InvalidMessageException{
        int length = message.getMessageText().length();
        Message upMessage = findMessage(message_id);
        if(length > 0 && length <= 255 && upMessage != null){
            upMessage.setMessageText(message.getMessageText());
            return upMessage;
        }
        throw new InvalidMessageException("Invalid message!");
    }

    public List<Message> getAllUserMessages(int user_id){
        List<Message> allUserMessages = new ArrayList<Message>();

        for(Message message:messageRepository.findAll()){
            if(message.getPostedBy().equals(user_id)) {
                allUserMessages.add(message);
            }
        }
        return allUserMessages;
    }
    
}
