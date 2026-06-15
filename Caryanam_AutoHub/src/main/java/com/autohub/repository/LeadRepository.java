package com.autohub.repository;

import com.autohub.entity.Lead;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead,Long> {

    List<Lead> findByDealerId(Long dealerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Lead l WHERE l.vehicle.id = :vehicleId")
    void deleteLeadsByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query("""
        SELECT MONTH(l.enquiryDate),
               COUNT(l),
               SUM(CASE WHEN l.leadStatus = 'CONVERTED' THEN 1 ELSE 0 END)
        FROM Lead l
        WHERE l.dealer.id = :dealerId
        AND YEAR(l.enquiryDate) = YEAR(CURRENT_DATE)
        GROUP BY MONTH(l.enquiryDate)
        ORDER BY MONTH(l.enquiryDate)
    """)
    List<Object[]> getMonthlyLeadAnalytics(@Param("dealerId") Long dealerId);

}
