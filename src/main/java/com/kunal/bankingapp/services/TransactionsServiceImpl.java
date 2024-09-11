package com.kunal.bankingapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kunal.bankingapp.dto.TransactionsDto;
import com.kunal.bankingapp.entity.Transactions;
import com.kunal.bankingapp.repository.TransactionRepository;

@Service
public class TransactionsServiceImpl implements TransactionsService {


    @Autowired
    TransactionRepository transactionRepository;


    @Override
    public void saveTransaction(TransactionsDto transactionsDto) {
        Transactions transactions = Transactions.builder()
        .transactionType(transactionsDto.getTransactionType())
        .accountNumber(transactionsDto.getAccountNumber())
        .amount(transactionsDto.getAmount())
        .status("SUCCESS")
        .build();

        transactionRepository.save(transactions);
    }

}
