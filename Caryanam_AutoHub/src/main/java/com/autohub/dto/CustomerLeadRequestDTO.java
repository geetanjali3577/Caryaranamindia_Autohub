package com.autohub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerLeadRequestDTO {

    @NotBlank(message = "Customer Name is Required")
    private String customerName;

    @NotBlank(message = "Customer Mobile is Required")
    private String customerMobile;

    @NotBlank(message = "Customer Email is Required")
    private String customerEmail;

    @NotBlank(message = "Customer City is Required")
    private String customerCity;

    @NotBlank(message = "Password is Required")
    private String customerPassword;

}
