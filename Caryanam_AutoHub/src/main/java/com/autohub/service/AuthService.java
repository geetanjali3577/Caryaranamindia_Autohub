package com.autohub.service;


import com.autohub.dto.LoginRequestDTO;
import com.autohub.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO dto);

    void logout(String token);


}
