package com.autohub.repository;

import com.autohub.entity.Payment;
import com.autohub.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByDealerId(String dealerId);
    Optional<Payment> findTopByDealerIdAndPaymentStatusOrderByPaymentIdDesc(Long dealerId, PaymentStatus paymentStatus);

    @Query("""
            SELECT p.subscriptionPlan,
                   COUNT(p.paymentId),
                   SUM(p.amount)
            FROM Payment p
            WHERE p.paymentStatus='SUCCESS'
            GROUP BY p.subscriptionPlan
            """)
    List<Object[]> getRevenueByPlanReport();
}
