package com.autohub.repository;

import com.autohub.entity.Vehicle;
import com.autohub.enums.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    long count();

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

    List<Vehicle> findTop10ByVehicleStatusOrderByIdDesc(VehicleStatus vehicleStatus);


    List<Vehicle> findTop10ByOrderByCreatedAtDesc();

    @Query("""
    SELECT v
    FROM Vehicle v
    WHERE v.vehicleStatus IN ('ACTIVE', 'FEATURED')
    """)
    List<Vehicle> findAllActiveAndFeaturedVehicles();

//    @Query(value = """
//        SELECT *
//        FROM vehicle_info
//        WHERE vehicle_status IN ('ACTIVE', 'FEATURED')
//        ORDER BY created_at DESC
//        LIMIT 10
//        """, nativeQuery = true)
//    List<Vehicle> findLatestActiveAndFeaturedVehicles();

    List<Vehicle> findTop10ByVehicleStatusInOrderByCreatedAtDesc(
            List<VehicleStatus> statuses
    );

}