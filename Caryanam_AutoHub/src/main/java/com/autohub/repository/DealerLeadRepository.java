package com.autohub.repository;


import com.autohub.entity.DealerLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealerLeadRepository
        extends JpaRepository<DealerLead, Long> {

    Long countByDealer_Id(Long dealerId);
}
