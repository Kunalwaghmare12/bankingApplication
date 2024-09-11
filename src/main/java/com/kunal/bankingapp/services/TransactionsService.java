package com.kunal.bankingapp.services;

import com.kunal.bankingapp.dto.TransactionsDto;

public interface TransactionsService {
    void saveTransaction(TransactionsDto transactionsDto);
}
