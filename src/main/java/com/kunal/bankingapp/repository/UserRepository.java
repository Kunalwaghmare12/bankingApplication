package com.kunal.bankingapp.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.kunal.bankingapp.entity.User;


public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
    
    @Query("SELECT u.accountBalance FROM users u WHERE u.accountNumber = :accountNumber")
    BigDecimal findAccountBalanceByAccountNumber(String accountNumber);

    @Modifying
    @Query("UPDATE users u SET u.accountBalance = u.accountBalance + :amount WHERE u.accountNumber = :accountNumber")
    void creditAccountInDB(String accountNumber,BigDecimal amount);

    @Modifying
    @Query("UPDATE users u SET u.accountBalance = u.accountBalance - :amount WHERE u.accountNumber = :accountNumber")
    void debitAccountInDB(String accountNumber,BigDecimal amount);

}
