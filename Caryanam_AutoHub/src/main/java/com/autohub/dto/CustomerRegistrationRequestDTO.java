package com.autohub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRegistrationRequestDTO {

    @NotBlank(message = "Customer Name is Required")
    private String customerName;

    @NotBlank(message = "Customer Mobile Number is Required")
    private String mobile;

    @NotBlank(message = "Customer City is Required")
    private String customerCity;

    @NotBlank(message = "Customer Email is Required")
    private String email;

    @NotBlank(message = "Customer Password is Required")
    private String password;


}
