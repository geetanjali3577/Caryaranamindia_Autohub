package com.autohub.service;

import com.autohub.dto.ResetPasswordDTO;
import com.autohub.dto.UserRegisterDTO;
import com.autohub.dto.UserResponseDTO;
import com.autohub.dto.VerifyOtpDTO;
import com.autohub.entity.Vehicle;

import java.util.List;

public interface UserService {

    //========================================
    // REGISTER USER
    //========================================
    UserResponseDTO registerUser(
            UserRegisterDTO dto
    );

    //========================================
    // GENERATE APPLICATION ID
    //========================================
    String generateApplicationId();

    UserResponseDTO searchByEmail(String email);
    UserResponseDTO getUserById(Long id);

    //List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRegisterDTO dto);

    List<UserResponseDTO> searchByName(String name);

   // void deleteUser(Long id);

    String sendOtp(String email);

    String verifyOtp(VerifyOtpDTO dto);

    String resetPassword(ResetPasswordDTO dto);

    void assignBankAndSendMail(
            Long userId,
            Long bankId
    );
    //.......................
    void paymentSuccess(Long userId);

    List<Vehicle> getAllActiveVehicles();
}