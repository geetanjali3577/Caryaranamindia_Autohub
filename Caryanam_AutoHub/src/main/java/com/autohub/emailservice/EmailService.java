package com.autohub.emailservice;

import com.autohub.entity.User;

import java.util.List;

public interface EmailService {


    void sendMail(String to, String subject, String body);

}