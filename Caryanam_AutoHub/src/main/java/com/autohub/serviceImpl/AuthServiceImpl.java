package com.autohub.serviceImpl;

import com.autohub.configuration.JwtUtil;
import com.autohub.dto.LoginRequestDTO;
import com.autohub.dto.LoginResponseDTO;
import com.autohub.entity.Admin;
import com.autohub.entity.Customer;
import com.autohub.entity.CustomerLead;
import com.autohub.entity.Dealer;
import com.autohub.repository.AdminRepository;
import com.autohub.repository.CustomerLeadRepository;
import com.autohub.repository.CustomerRepository;
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

    //private final CustomerLeadRepository leadRepository;

    private final CustomerRepository customerRepository;

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
                        || customerRepository.existsByEmail(request.getEmail());


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
                    dealer.getRole(),
                    dealer.getMobile(),
                    dealer.getCity()
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
                    admin.getRole(),
                    admin.getMobileNumber(),
                    admin.getCity()
            );

            return new LoginResponseDTO(token);
        }
        // =====================================
        // CUSTOMER LOGIN
        // =====================================


        Optional<Customer> byEmail = customerRepository.findByEmail(request.getEmail());

        if (byEmail.isPresent()) {

            Customer customer = byEmail.get();

            String token = jwtUtil.generateToken(
                    customer.getId(),
                    customer.getCustomerName(),
                    customer.getEmail(),
                    customer.getRole(),
                    customer.getMobile(),
                    customer.getCustomerCity()
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