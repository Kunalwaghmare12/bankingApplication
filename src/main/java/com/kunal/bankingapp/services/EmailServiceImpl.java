package com.kunal.bankingapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.kunal.bankingapp.dto.EmailDetails;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EmailServiceImpl implements EmailService {


    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipients());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            log.info("Mail sent sucessfully");
        } catch (Exception e) {
            throw e;
        }
    }

    

}
