package com.kunal.bankingapp.services;

import com.kunal.bankingapp.dto.AccountInfo;
import com.kunal.bankingapp.dto.BankResponse;
import com.kunal.bankingapp.dto.CreditDebitRequest;
import com.kunal.bankingapp.dto.EmailDetails;
import com.kunal.bankingapp.dto.EnquiryRequest;
import com.kunal.bankingapp.dto.UserRequest;
import com.kunal.bankingapp.entity.User;
import com.kunal.bankingapp.repository.UserRepository;
import com.kunal.utils.AccountUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        // Creating an account - saving a new user into database
        // check if user already has an account

        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                .accountInfo(null)
                .build();
                
            
        }
       
        User newUser = User.builder()
        .firstName(userRequest.getFirstName())
        .lastName(userRequest.getLastName())
        .gender(userRequest.getGender())
        .address(userRequest.getAddress())
        .stateOfOrigin(userRequest.getStateOfOrigin())
        .accountNumber(AccountUtils.generateAccountNumber())
        .accountBalance(userRequest.getAccountBalance())
        .email(userRequest.getEmail())
        .phoneNumber(userRequest.getPhoneNumber())
        .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
        .status("ACTIVE")
        .build();

        User savedUser = userRepository.save(newUser);
        
        // //send email
        // EmailDetails emailDetails = EmailDetails.builder()
        //         .recipients(savedUser.getEmail())
        //         .subject("Account Creation")
        //         .messageBody("Congratulation! Your Account Has been Sucessfully Created. \nYour Account Details :\nAccount Name : "+ savedUser.getFirstName()+" "+savedUser.getLastName()+"\nAccount Number : "+ savedUser.getAccountNumber())
        //         .build();
        // emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
            .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
            .accountInfo(AccountInfo.builder()
                .accountBalance(savedUser.getAccountBalance())
                .accountNumber(savedUser.getAccountNumber())
                .accountName(savedUser.getFirstName()+" "+savedUser.getLastName())

                .build())
            .build();
          
    }



    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        // check if the provided account number exists
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
            .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
            .accountInfo(null)
            .build();
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return BankResponse.builder()
            .responseCode(AccountUtils.USER_FOUND_CODE)
            .responseMessage(AccountUtils.USER_FOUND_MESSAGE)
            .accountInfo(AccountInfo.builder()
                .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                .accountNumber(foundUser.getAccountNumber())
                .accountBalance(foundUser.getAccountBalance())
                .build())
            .build();
                
    }



    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        // check account with the name exists in db

        boolean isAccountExists=userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName()+" "+foundUser.getLastName();

    }



    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                .accountInfo(null)
                .build();
        }
        // Amount in database is not reflecting
        User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));

        return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_CREDITE_SUCCESS_CODE)
            .responseMessage(AccountUtils.ACCOUNT_CREDITE_SUCCESS_MESSAGE)
            .accountInfo(AccountInfo.builder()
                .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName())
                .accountNumber(userToCredit.getAccountNumber())
                .accountBalance(userToCredit.getAccountBalance())
                .build())
            .build();

    }

    

}
