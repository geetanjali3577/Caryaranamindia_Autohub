package com.autohub.repository;

import com.autohub.entity.OlxCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OlxCarRepository extends JpaRepository<OlxCar, Long> {
}