package com.autohub.configuration;

import java.util.List;


import com.autohub.entity.Admin;

import com.autohub.entity.Customer;
import com.autohub.entity.CustomerLead;
import com.autohub.entity.Dealer;
import com.autohub.repository.AdminRepository;

import com.autohub.repository.CustomerLeadRepository;
import com.autohub.repository.CustomerRepository;
import com.autohub.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final AdminRepository adminRepository;

    private final DealerRepository dealerRepository;

    private final CustomerRepository customerRepository;


    @Override
    public CustomUserDetails loadUserByUsername(String mobile)
            throws UsernameNotFoundException {


        // ADMIN
//        Admin admin = adminRepository.findByEmail(email).orElse(null);
        Admin admin = adminRepository.findByMobile(mobile).orElse(null);

        if (admin != null) {

            String role = admin.getRole() != null
                    ? admin.getRole().name()
                    : "ADMIN";

            return new CustomUserDetails(
                    admin.getAdminId(),
                    admin.getFullName(),
                    admin.getEmail(),
                    admin.getMobile(),
                    admin.getPassword(),
                    role,
                    List.of(
                            new SimpleGrantedAuthority("ROLE_" + role)
                    )
            );
        }

        //Customer

//        Customer customer =
//                customerRepository.findByEmail(email)
//                        .orElse(null);

        Customer customer =
                customerRepository.findByMobile(mobile)
                        .orElse(null);
        if (customer != null) {

            String role = customer.getRole() != null
                    ? customer.getRole().name()
                    : "CUSTOMER";

            return new CustomUserDetails(
                    customer.getId(),
                    customer.getCustomerName(),
                    customer.getEmail(),
                    customer.getMobile(),
                    customer.getPassword(),
                    role,
                    List.of(
                            new SimpleGrantedAuthority("ROLE_" + role)
                    )
            );
        }




//        // DEALER
//        Dealer dealer = dealerRepository.findByEmail(email).orElse(null);

        Dealer dealer = dealerRepository.findByMobile(mobile).orElse(null);

        if (dealer != null) {

            String role = dealer.getRole() != null
                    ? dealer.getRole().name()
                    : "DEALER";

            return new CustomUserDetails(
                    dealer.getId(),
                    dealer.getOwnerName(),
                    dealer.getEmail(),
                    dealer.getMobile(),
                    dealer.getPassword(),
                    role,
                    List.of(
                            new SimpleGrantedAuthority("ROLE_" + role)
                    )
            );
        }

        throw new UsernameNotFoundException(
                "User not found with mobile: " + mobile
        );
    }
}