package com.kunal.utils;

import java.time.Year;
import java.util.Random;

public class AccountUtils {

    private static Random random = new Random();
    private AccountUtils() {
        // Private constructor to prevent instantiation
    }

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";
    public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with the provided Account Number does not exist";
    public static final String USER_FOUND_CODE = "004";
    public static final String USER_FOUND_MESSAGE = "User Account Found";
    public static final String ACCOUNT_CREDITE_SUCCESS_CODE = "005";
    public static final String ACCOUNT_CREDITE_SUCCESS_MESSAGE = "Account has been credited Sucessfully!";
    public static final String ACCOUNT_DEBIT_SUCCESS_CODE = "006";
    public static final String ACCOUNT_DEBIT_SUCCESS_MESSAGE = "Account has been debited Sucessfully!";
    public static final String ACCOUNT_INSUFFICIENT_CODE = "007";
    public static final String ACCOUNT_INSUFFICIENT_MESSAGE = "Insufficient Funds!";
    public static final String FUND_TRANSFER_CODE = "008";
    public static final String FUND_TRANSFER_MESSAGE = "Fund Transfer Sucessfully!";

    public static String generateAccountNumber() {
        //2023 + randomSixDigit
        Year currentYear = Year.now();
        // Generate a random number between 100000 and 999999
        int randomSixDigit = 100000 + random.nextInt(900000); // (999999 - 100000 + 1) = 900000
        // Convert currentYear and randomSixDigit into strings and concatenate
        String year = String.valueOf(currentYear.getValue());
        String randomNumber = String.valueOf(randomSixDigit);
        // Combine year and random number into a new account number
        return year + randomNumber;
    }

   
}
