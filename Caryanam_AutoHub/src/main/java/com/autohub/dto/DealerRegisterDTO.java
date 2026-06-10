package com.autohub.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DealerRegisterDTO {

    @NotBlank(message = "Business Name is Required")
    private String businessName;

    @NotBlank(message = "Owner Name is Required")
    private String ownerName;

    @NotBlank(message = "GST Number is Required")
    private String gstNumber;

   @NotNull(message = "Years In Business is Required")
    private Integer yearsInBusiness;

    @NotBlank(message = "Mobile Number is Required")
    private String mobile;

    @NotBlank(message = "WhatsApp Number is Required")
    private String whatsapp;

   @NotBlank(message = "Email is Required")
   @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Password is Required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Address is Required")
    private String address;

    @NotBlank(message = "City is Required")
    private String city;

    @NotBlank(message = "State is Required")
    private String state;

    @NotBlank(message = "PinCode is Required")
    private String pinCode;


    //Add Validation as Image
    private String dealerLogo;
    private String showroomImage;
}
