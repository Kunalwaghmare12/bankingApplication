package com.kunal.bankingapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDetails {

    private String recipients;
    private String messageBody;
    private String subject;
    private String attachment;
}
