package com.kunal.bankingapp.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kunal.bankingapp.config.JwtTokenProvider;
import com.kunal.bankingapp.dto.AccountInfo;
import com.kunal.bankingapp.dto.BankResponse;
import com.kunal.bankingapp.dto.CreditDebitRequest;
import com.kunal.bankingapp.dto.EmailDetails;
import com.kunal.bankingapp.dto.EnquiryRequest;
import com.kunal.bankingapp.dto.LoginDto;
import com.kunal.bankingapp.dto.TransactionsDto;
import com.kunal.bankingapp.dto.TransferRequest;
import com.kunal.bankingapp.dto.UserRequest;
import com.kunal.bankingapp.entity.Role;
import com.kunal.bankingapp.entity.User;
import com.kunal.bankingapp.repository.UserRepository;
import com.kunal.bankingapp.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    TransactionsService transactionsService;

    @Autowired 
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        // Creating an account - saving a new user into database
        // check if user already has an account

        if (userRepository.existsByEmail(userRequest.getEmail())) {
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
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_ADMIN"))
                .build();

        User savedUser = userRepository.save(newUser);

        // //send email
        // EmailDetails emailDetails = EmailDetails.builder()
        // .recipients(savedUser.getEmail())
        // .subject("Account Creation")
        // .messageBody("Congratulation! Your Account Has been Sucessfully Created.
        // \nYour Account Details :\nAccount Name : "+ savedUser.getFirstName()+"
        // "+savedUser.getLastName()+"\nAccount Number : "+
        // savedUser.getAccountNumber())
        // .build();
        // emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())

                        .build())
                .build();

    }

    public BankResponse login(LoginDto loginDto){
        Authentication authentication =null;
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        
        // EmailDetails loginAlert = EmailDetails.builder()
        //         .subject("you are logged in!")
        //         .messageBody("You logged into your account. if you did not initiate this request, please contact to Bank")
        //         .build();
        
        //         emailService.sendEmailAlert(loginAlert);

                return BankResponse.builder()
                        .responseCode("Login Sucess!")
                        .responseMessage(jwtTokenProvider.generateToken(authentication))
                        .build();

}

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        // check if the provided account number exists
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExists) {
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
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        // check account with the name exists in db

        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExists) {
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();

    }

    @Override
    @Transactional
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userRepository.creditAmmountInDB(creditDebitRequest.getAccountNumber(), creditDebitRequest.getAmount());
        BigDecimal updatedBalance = userRepository
                .findAccountBalanceByAccountNumber(creditDebitRequest.getAccountNumber());

        // save Transcation
        TransactionsDto transactionsDto = TransactionsDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(creditDebitRequest.getAmount())
                .build();

        transactionsService.saveTransaction(transactionsDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITE_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITE_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountBalance(updatedBalance)
                        .build())
                .build();

    }

    @Override
    @Transactional
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());

        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        BigDecimal availabelBalance = userRepository
                .findAccountBalanceByAccountNumber(creditDebitRequest.getAccountNumber());
        if (availabelBalance.compareTo(creditDebitRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();

        }
        userRepository.debitAmmountInDB(creditDebitRequest.getAccountNumber(), creditDebitRequest.getAmount());
        BigDecimal updatedBalance = userRepository
                .findAccountBalanceByAccountNumber(creditDebitRequest.getAccountNumber());

        // save Transcation
        TransactionsDto transactionsDto = TransactionsDto.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(creditDebitRequest.getAmount())
                .build();

        transactionsService.saveTransaction(transactionsDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                        .accountNumber(userToDebit.getAccountNumber())
                        .accountBalance(updatedBalance)
                        .build())
                .build();

    }

    @Override
    @Transactional
    public BankResponse transfer(TransferRequest transferRequest) {
        boolean isSourceAccountExists = userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber());
        boolean isDestinationAccountExists = userRepository
                .existsByAccountNumber(transferRequest.getDestinationAccountNumber());

        if (!isSourceAccountExists && !isDestinationAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .build();
        } else if (isDestinationAccountExists && !isSourceAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage("Source Account :" + AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .build();

        } else if (!isDestinationAccountExists && isSourceAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage("Destination Account :" + AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .build();
        } else {
            User sourceUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
            User destinationUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());

            if (sourceUser.getAccountBalance().compareTo(transferRequest.getAmount()) < 0) {
                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_MESSAGE)
                        .build();
            } else {
                destinationUser.setAccountBalance(destinationUser.getAccountBalance().add(transferRequest.getAmount()));
                userRepository.debitAmmountInDB(sourceUser.getAccountNumber(), transferRequest.getAmount());
                userRepository.creditAmmountInDB(destinationUser.getAccountNumber(), transferRequest.getAmount());
                return BankResponse.builder()
                        .responseCode(AccountUtils.FUND_TRANSFER_CODE)
                        .responseMessage(AccountUtils.FUND_TRANSFER_MESSAGE)
                        .build();
            }
        }
    }

}
