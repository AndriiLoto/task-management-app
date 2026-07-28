package com.example.taskmanagementapp.service.telegram.impl;

import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.exception.TelegramLinkTokenExpiredException;
import com.example.taskmanagementapp.model.TelegramLinkToken;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.TelegramLinkTokenRepository;
import com.example.taskmanagementapp.repository.UserRepository;
import com.example.taskmanagementapp.service.telegram.TelegramLinkService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramLinkServiceImpl implements TelegramLinkService {
    private final TelegramLinkTokenRepository telegramLinkTokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public String generateLinkToken(User user) {
        String generateToken = RandomStringGenerator.builder()
                .withinRange('0','z')
                .filteredBy(Character::isLetterOrDigit)
                .get()
                .generate(7);

        TelegramLinkToken token = telegramLinkTokenRepository
                .findByUserId(user.getId())
                .orElseGet(TelegramLinkToken::new);
        token.setToken(generateToken);
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        telegramLinkTokenRepository.save(token);
        return generateToken;
    }

    @Override
    @Transactional
    public void linkTelegram(String token, Long chatId) {
        TelegramLinkToken linkToken = telegramLinkTokenRepository
                .findTelegramLinkTokenByToken(token)
                .orElseThrow(
                    () -> new EntityNotFoundException("Telegram link token not found with token "
                            + token)
        );
        if (linkToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            telegramLinkTokenRepository.delete(linkToken);
            throw new TelegramLinkTokenExpiredException("Telegram link token has expired");
        }
        User user = linkToken.getUser();
        user.setTelegramChatId(chatId);
        userRepository.save(user);

        telegramLinkTokenRepository.delete(linkToken);
    }
}
