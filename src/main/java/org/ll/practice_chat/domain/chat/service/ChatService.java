package org.ll.practice_chat.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.ll.practice_chat.domain.chat.SseEmitters;
import org.ll.practice_chat.domain.chat.entity.Chat;
import org.ll.practice_chat.domain.chat.repository.ChatRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final SseEmitters sseEmitters;
    private final SimpMessagingTemplate simpleMessagingTemplete;

    @Transactional
    public Chat writeMessage(String name, String content) {
        Chat chat = Chat.builder()
                .name(name)
                .content(content)
                .createDate(LocalDateTime.now())
                .build();

        return chatRepository.save(chat);
    }

    @Transactional
    public Chat writeMessageSse(String name, String content) {
        Chat chat = Chat.builder()
                .name(name)
                .content(content)
                .createDate(LocalDateTime.now())
                .build();

        sseEmitters.noti("chat__messageAdded");

        return chatRepository.save(chat);
    }

    @Transactional
    public Chat writeMessageWebSocket(String name, String content) {
        Chat chat = Chat.builder()
                .name(name)
                .content(content)
                .createDate(LocalDateTime.now())
                .build();

        simpleMessagingTemplete.convertAndSend("/topic/chat/writeMessage", chat);

        return chatRepository.save(chat);
    }

    @Transactional
    public List<Chat> getMessages(Long fromId) {
        if (chatRepository.existsById(fromId)) {
            return chatRepository.findAll().stream()
                    .dropWhile(chat -> !chat.getId().equals(fromId))
                    .skip(1)
                    .collect(Collectors.toList());
        }

        return chatRepository.findAll();
    }

    public int getTotalCount() {
        return (int) chatRepository.count();
    }
}
