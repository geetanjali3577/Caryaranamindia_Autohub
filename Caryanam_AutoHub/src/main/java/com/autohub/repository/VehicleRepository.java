package com.autohub.repository;

import com.autohub.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    long count();

    Optional<Vehicle> findByVehicleId(String vehicleId);

    Long countByDealerId(String dealerId);

    @Query("""
    SELECT v
    FROM Vehicle v
    WHERE v.dealerId IN (
        SELECT d.dealerCode
        FROM Dealer d
        WHERE d.subscriptionActive = true
    )
    """)
    List<Vehicle> getAllActiveVehicles();
}