package com.autohub.service;

import com.autohub.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


public interface DealerService {

    DealerResponseDTO registerDealer(DealerRegisterDTO dto, MultipartFile dealerLogo,MultipartFile showroomImage);

    List<DealerSubscriptionResponseDTO> getSubscriptions();


    DealerProfileResponseDTO updateDealerProfile(Long id, UpdateDealerProfileRequestDTO dto);

    String sendOtp(String email);

    String verifyOtp(VerifyOtpDTO dto);

    String resetPassword(ResetPasswordDTO dto);

    DashboardResponseDTO getDashboard(Long dealerId);

}