package com.kunal.bankingapp.services;


import com.kunal.bankingapp.dto.BankResponse;
import com.kunal.bankingapp.dto.CreditDebitRequest;
import com.kunal.bankingapp.dto.EnquiryRequest;
import com.kunal.bankingapp.dto.LoginDto;
import com.kunal.bankingapp.dto.TransferRequest;
import com.kunal.bankingapp.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
    BankResponse transfer(TransferRequest transferRequest);

    BankResponse login(LoginDto loginDto);
}
