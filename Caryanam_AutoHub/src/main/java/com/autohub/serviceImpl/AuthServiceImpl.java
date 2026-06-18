package com.autohub.serviceImpl;

import com.autohub.configuration.JwtUtil;
import com.autohub.dto.LoginRequestDTO;
import com.autohub.dto.LoginResponseDTO;
import com.autohub.entity.Admin;
import com.autohub.entity.CustomerLead;
import com.autohub.entity.Dealer;
import com.autohub.repository.AdminRepository;
import com.autohub.repository.CustomerLeadRepository;
import com.autohub.repository.DealerRepository;
import com.autohub.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private final DealerRepository dealerRepository;
    private final AdminRepository adminRepository;

    private final CustomerLeadRepository leadRepository;

    // TOKEN BLACKLIST
    private static final Set<String> tokenBlacklist = new HashSet<>();

    // =====================================================
    // LOGIN
    // =====================================================
    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        // =====================================
        // EMAIL EXISTS CHECK
        // =====================================

        boolean emailExists = dealerRepository.existsByEmail(request.getEmail())
                        || adminRepository.existsByEmail(request.getEmail())
                        || leadRepository.existsByCustomerEmail(request.getEmail());

        System.out.println("Email = " + request.getEmail());

        System.out.println("Dealer Exists = "
                + dealerRepository.existsByEmail(request.getEmail()));

        System.out.println("Admin Exists = "
                + adminRepository.existsByEmail(request.getEmail()));

        System.out.println("Customer Exists = "
                + leadRepository.existsByCustomerEmail(request.getEmail()));

        if (!emailExists) {

            throw new RuntimeException(
                    "Account not found. Please register first."
            );
        }

        // =====================================
        // PASSWORD AUTHENTICATION
        // =====================================

        try {

            authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid Password"
            );
        }


        // =====================================
        // DEALER LOGIN
        // =====================================

        Optional<Dealer> dealerOpt =
                dealerRepository.findByEmail(request.getEmail());

        if (dealerOpt.isPresent()) {

            Dealer dealer = dealerOpt.get();

            String token = jwtUtil.generateToken(
                    dealer.getId(),
                    dealer.getOwnerName(),
                    dealer.getEmail(),
                    dealer.getRole()
            );

            return new LoginResponseDTO(token);
        }

        // =====================================
        // ADMIN LOGIN
        // =====================================

        Optional<Admin> adminOpt =
                adminRepository.findByEmail(request.getEmail());

        if (adminOpt.isPresent()) {

            Admin admin = adminOpt.get();

            String token = jwtUtil.generateToken(
                    admin.getAdminId(),
                    admin.getFullName(),
                    admin.getEmail(),
                    admin.getRole()
            );

            return new LoginResponseDTO(token);
        }
        // =====================================
        // CUSTOMER LOGIN
        // =====================================

        Optional<CustomerLead> customerOpt =
                leadRepository.findByCustomerEmail(request.getEmail());

        if (customerOpt.isPresent()) {

            CustomerLead customer = customerOpt.get();

            String token = jwtUtil.generateToken(
                    customer.getId(),
                    customer.getCustomerName(),
                    customer.getCustomerEmail(),
                    customer.getRole()
            );

            return new LoginResponseDTO(token);
        }



        throw new RuntimeException(
                "Login Failed"
        );
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    @Override
    public void logout(String token) {

        if (token == null || token.isBlank()) {

            throw new RuntimeException(
                    "Authorization Token is Missing"
            );
        }

        if (token.startsWith("Bearer ")) {

            token = token.substring(7);
        }

        tokenBlacklist.add(token);

        System.out.println("Logout Successfully");
    }

    // =====================================================
    // CHECK TOKEN
    // =====================================================
    public static boolean isTokenBlacklisted(String token) {

        return tokenBlacklist.contains(token);
    }
}