package com.autohub.service;

import com.autohub.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


public interface DealerService {

    DealerResponseDTO registerDealer(DealerRegisterDTO dto);


    String sendOtp(String email);

    String verifyOtp(VerifyOtpDTO dto);

    String resetPassword(ResetPasswordDTO dto);

   // DealerResponseDTO updateDealer(Long id, DealerRegisterDTO dto);

   // List<DealerResponseDTO> getAllDealers();

    //DealerResponseDTO searchByDealerCode(String dealerCode);

}