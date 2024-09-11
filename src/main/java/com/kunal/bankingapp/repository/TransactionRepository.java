package com.kunal.bankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kunal.bankingapp.entity.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions,String> {

}
