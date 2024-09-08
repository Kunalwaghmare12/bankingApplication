package com.kunal.bankingapp.services;

import com.kunal.bankingapp.dto.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);
}
