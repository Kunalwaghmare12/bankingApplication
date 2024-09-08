package com.kunal.bankingapp.services;


import com.kunal.bankingapp.dto.BankResponse;
import com.kunal.bankingapp.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
}
