package com.kunal.bankingapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kunal.bankingapp.dto.BankResponse;
import com.kunal.bankingapp.dto.CreditDebitRequest;
import com.kunal.bankingapp.dto.EnquiryRequest;
import com.kunal.bankingapp.dto.TransferRequest;
import com.kunal.bankingapp.dto.UserRequest;
import com.kunal.bankingapp.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // @Autowired
    // this is constructor 
    public UserController(UserService userService){
        this.userService=userService;
    }


    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);

    }

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit")
    public BankResponse creditAmount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.creditAccount(creditDebitRequest);
    }
    @PostMapping("/debit")
    public BankResponse debitAmount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.debitAccount(creditDebitRequest);
    }

    @PostMapping("/transfer")
    public BankResponse transferAmount(@RequestBody TransferRequest transferRequest){
        return userService.transfer(transferRequest);

    }
}
