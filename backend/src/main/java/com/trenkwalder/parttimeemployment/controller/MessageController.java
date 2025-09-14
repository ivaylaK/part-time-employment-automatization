package com.trenkwalder.parttimeemployment.controller;

import com.trenkwalder.parttimeemployment.dto.MessageRequestDto;
import com.trenkwalder.parttimeemployment.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/messages")
public class MessageController {

    private final MessageService messageService;


    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(path = "/sendSmsToOne")
    public ResponseEntity<String> sendSmsToOneApplicant(@RequestBody MessageRequestDto messageRequest) {
        messageService.sendSmsToOneApplicant(messageRequest.getMessage(), messageRequest.getNumbers().getFirst());
        return new ResponseEntity<>("Message sent successfully.", HttpStatus.OK);
    }

    @PostMapping(path = "/sendSmsToAll")
    public ResponseEntity<String> sendSmsToAllApplicants(@RequestBody MessageRequestDto messageRequest) {
        messageService.sendSmsToAllApplicants(messageRequest.getMessage());
        return new ResponseEntity<>("Messages sent successfully.", HttpStatus.OK);
    }

    @PostMapping(path = "/sendViberMessageToOne")
    public ResponseEntity<String> sendViberMessageToOneApplicant(@RequestBody MessageRequestDto messageRequest) {
        messageService.sendViberMessageToOneApplicant(messageRequest.getMessage(), messageRequest.getNumbers().getFirst());
        return new ResponseEntity<>("Message sent successfully.", HttpStatus.OK);
    }

    @PostMapping(path = "/sendViberMessageToAll")
    public ResponseEntity<String> sendViberMessageToAllApplicants(@RequestBody MessageRequestDto messageRequest) {
        messageService.sendViberMessageToAllApplicants(messageRequest.getMessage());
        return new ResponseEntity<>("Messages sent successfully.", HttpStatus.OK);
    }
}