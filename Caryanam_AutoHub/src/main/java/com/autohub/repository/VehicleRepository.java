package com.autohub.repository;

import com.autohub.entity.Vehicle;
import com.autohub.enums.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    long count();

   //Optional<Vehicle> findByVehicleId(String vehicleId);


    Long countByDealer_Id(Long dealerId);

    @Query("""
        SELECT v
        FROM Vehicle v
        WHERE v.dealer.subscriptionActive = true
    """)
    List<Vehicle> getAllActiveVehicles();

    Long countByDealer_IdAndVehicleStatus(Long dealerId, VehicleStatus status);
    long countByDealerId(Long dealerId);


    List<Vehicle> findByDealerId(Long dealerId);
}