package com.autohub.repository;

import com.autohub.entity.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatConversationRepository
        extends JpaRepository<ChatConversation,Long> {

    @Query("""
    SELECT c
    FROM ChatConversation c
    WHERE
    (
        c.user1Id = :user1
        AND c.user2Id = :user2
    )
    OR
    (
        c.user1Id = :user2
        AND c.user2Id = :user1
    )
    """)
    Optional<ChatConversation>
    findConversation(
            Long user1,
            Long user2
    );

    List<ChatConversation>
    findByUser1IdOrUser2Id(
            Long user1,
            Long user2
    );
}
