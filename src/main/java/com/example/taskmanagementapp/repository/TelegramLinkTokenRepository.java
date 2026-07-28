package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.TelegramLinkToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramLinkTokenRepository extends JpaRepository<TelegramLinkToken, Long> {
    Optional<TelegramLinkToken> findTelegramLinkTokenByToken(String token);

    Optional<TelegramLinkToken> findByUserId(Long id);
}
