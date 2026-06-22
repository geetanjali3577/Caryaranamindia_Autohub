package com.autohub.serviceImpl;

import com.autohub.dto.CustomerWishlistDTO;
import com.autohub.dto.DealerWishlistDTO;
import com.autohub.entity.Customer;
import com.autohub.entity.Vehicle;
import com.autohub.entity.Wishlist;
import com.autohub.exception.ResourceNotFoundException;
import com.autohub.repository.CustomerRepository;
import com.autohub.repository.VehicleRepository;
import com.autohub.repository.WishlistRepository;
import com.autohub.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
//    private final CustomerLeadRepository customerRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public String addToWishlist(Long customerId, Long vehicleId) {

        Customer customer = customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException("Customer not found"));

        Vehicle vehicle =
                vehicleRepository.findById(vehicleId)
                        .orElseThrow(() ->
                                new RuntimeException("Vehicle not found"));

        if (wishlistRepository.existsByCustomerAndVehicle(customer, vehicle)) {
            return "Vehicle already added to wishlist";
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);
        wishlist.setVehicle(vehicle);
        wishlist.setAddedAt(LocalDateTime.now());

        wishlistRepository.save(wishlist);

        return "Vehicle added to wishlist successfully";
    }

    @Override
    public List<CustomerWishlistDTO> getCustomerWishlist(Long customerId) {

        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException("Customer not found"));

        return wishlistRepository.findByCustomer(customer)
                .stream()
                .map(w -> new CustomerWishlistDTO(
                        w.getVehicle().getId(),
                        w.getVehicle().getBrand(),
                        w.getVehicle().getModel()+" "+w.getVehicle().getVariant(),
                        w.getVehicle().getAskingPrice(),
                        w.getAddedAt()
                ))
                .toList();
    }

    @Override
    public List<DealerWishlistDTO> getDealerWishlist(Long dealerId) {

        List<Wishlist> wishlists =
                wishlistRepository.findDealerWishlist(dealerId);

        if (wishlists.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No wishlist vehicles found for dealer id : " + dealerId
            );
        }

        return wishlists.stream()
                .map(w -> new DealerWishlistDTO(
                        w.getVehicle().getId(),
                        w.getVehicle().getBrand()+" "+w.getVehicle().getModel()+" "+w.getVehicle().getVariant(),
                        w.getCustomer().getId(),
                        w.getCustomer().getCustomerName(),
                        w.getCustomer().getEmail(),
                        w.getCustomer().getMobile(),
                        w.getAddedAt()
                ))
                .toList();
    }

    @Override
    public String removeWishlist(Long customerId, Long vehicleId) {

        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException("Customer not found"));

        Vehicle vehicle =
                vehicleRepository.findById(vehicleId)
                        .orElseThrow(() ->
                                new RuntimeException("Vehicle not found"));

        Wishlist wishlist =
                wishlistRepository.findAll()
                        .stream()
                        .filter(w ->
                                w.getCustomer().getId().equals(customer.getId())
                                        &&
                                        w.getVehicle().getId().equals(vehicle.getId()))
                        .findFirst()
                        .orElseThrow(() ->
                                new RuntimeException("Wishlist not found"));

        wishlistRepository.delete(wishlist);

        return "Wishlist removed successfully";
    }
}