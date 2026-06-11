package com.autohub.repository;

import com.autohub.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    long count();

    Optional<Vehicle> findByVehicleId(String vehicleId);

}