package com.autohub.service;

import com.autohub.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


public interface DealerService {

    DealerResponseDTO registerDealer(DealerRegisterDTO dto, MultipartFile dealerLogo,MultipartFile showroomImage);

    DealerResponseDTO getDealerProfile(Long dealerId);

    List<DealerSubscriptionResponseDTO> getSubscriptions();

    DealerResponseDTO updateDealerAccountStatus(Long dealerId,DealerAccountStatusRequestDTO requestDTO);

    DealerProfileResponseDTO updateDealerProfile(Long id, UpdateDealerProfileRequestDTO dto);

    String sendOtp(String email);

    String verifyOtp(VerifyOtpDTO dto);

    String resetPassword(ResetPasswordDTO dto);

    DashboardResponseDTO getDashboard(Long dealerId);

}