package com.autohub.repository;

import com.autohub.entity.UserPresence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPresenceRepository
        extends JpaRepository<UserPresence,Long> {

    Optional<UserPresence>
    findByUserId(Long userId);
}
