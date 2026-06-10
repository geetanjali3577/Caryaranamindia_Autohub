package com.autohub.dto;



import com.autohub.enums.RegistrationType;
import lombok.Data;

@Data
public class UserRegisterDTO {

    private String fullName;

    private String email;

    private String mobileNumber;

    private String password;

    // DEALER or INDIVIDUAL
    private RegistrationType registrationType;

    // Required only for DEALER type
    //private String dealerCode;
    private String role;

}
