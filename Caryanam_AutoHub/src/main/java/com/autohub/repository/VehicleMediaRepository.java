package com.autohub.repository;

import com.autohub.entity.VehicleMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleMediaRepository extends JpaRepository<VehicleMedia, Long> {
}
