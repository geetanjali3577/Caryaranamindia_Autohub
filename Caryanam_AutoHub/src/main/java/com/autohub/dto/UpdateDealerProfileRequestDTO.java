package com.autohub.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateDealerProfileRequestDTO {

    @NotBlank(message = "Business Name is Required")
    private String businessName;

    @NotBlank(message = "Owner Name is Required")
    private String ownerName;

    @NotBlank(message = "Mobile Number is Required")
    private String mobile;

    @NotBlank(message = "WhatsAPP Number is Required")
    private String whatsapp;

    @NotBlank(message = "Address is Required")
    private String address;

    @NotBlank(message = "City is Required")
    private String city;

    @NotBlank(message = "Pin code is Required")
    private String pinCode;

    @NotBlank(message = "State is Required")
    private String state;


}
