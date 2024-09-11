package com.kunal.bankingapp.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsDto {
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;


}
