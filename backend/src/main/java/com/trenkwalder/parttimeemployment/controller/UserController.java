package com.trenkwalder.parttimeemployment.controller;

import com.trenkwalder.parttimeemployment.dto.LoginDto;
import com.trenkwalder.parttimeemployment.dto.UserDto;

import com.trenkwalder.parttimeemployment.entity.User;
import com.trenkwalder.parttimeemployment.mapper.JobMapper;
import com.trenkwalder.parttimeemployment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/authentication")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody @Valid UserDto dto) {
        try {
            User user = userService.signUp(dto);
            return new ResponseEntity<>(user.getEmail() + " registered!", HttpStatus.OK);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> signIn(@RequestBody @Valid LoginDto dto) {
        try {
            String token = userService.signIn(dto);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
