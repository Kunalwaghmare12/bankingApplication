package com.kunal.bankingapp.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kunal.bankingapp.dto.BankResponse;
import com.kunal.bankingapp.dto.UserRequest;
import com.kunal.bankingapp.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }


    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);

    }
}
