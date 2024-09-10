package com.kunal.bankingapp.services;


import com.kunal.bankingapp.dto.BankResponse;
import com.kunal.bankingapp.dto.CreditDebitRequest;
import com.kunal.bankingapp.dto.EnquiryRequest;
import com.kunal.bankingapp.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    // BankResponse updateAccount(UserRequest userRequest); // method yet to be created
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
}
