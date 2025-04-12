package org.ll.practice_chat.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.ll.practice_chat.domain.chat.dto.ChatDto;
import org.ll.practice_chat.domain.chat.entity.Chat;
import org.ll.practice_chat.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<ChatDto.WriteMessageResponse> writeMessage(@RequestBody ChatDto.WriteMessageRequest writeMessageRequest) {
        Chat chat = chatService.writeMessage(writeMessageRequest.getName(), writeMessageRequest.getContent());

        return ResponseEntity.ok(
                ChatDto.WriteMessageResponse.builder()
                        .chat(chat)
                        .build()
        );
    }

    @PostMapping("/write-sse")
    @ResponseBody
    public ResponseEntity<ChatDto.WriteMessageResponse> writeMessageSSE(@RequestBody ChatDto.WriteMessageRequest writeMessageRequest) {
        Chat chat = chatService.writeMessageSSE(writeMessageRequest.getName(), writeMessageRequest.getContent());

        return ResponseEntity.ok(
                ChatDto.WriteMessageResponse.builder()
                        .chat(chat)
                        .build()
        );
    }

    @GetMapping("/messages")
    @ResponseBody
    public ResponseEntity<ChatDto.MessagesResponse> messages(Long fromId) {
        return ResponseEntity.ok(
                ChatDto.MessagesResponse.builder()
                        .chats(chatService.getMessages(fromId))
                        .totalCount(chatService.getTotalCount())
                        .build()
        );
    }

    @GetMapping("/messages/long-polling")
    @ResponseBody
    public ResponseEntity<ChatDto.MessagesResponse> longPollingMessages(Long fromId) throws InterruptedException {
        int maxAttempts = 60; // 최대 60번 시도 (30초)
        int attempt = 0;
        List<Chat> chats;

        while (attempt < maxAttempts) {
            chats = chatService.getMessages(fromId);

            if (!chats.isEmpty()) {
                return ResponseEntity.ok(
                        ChatDto.MessagesResponse.builder()
                                .chats(chats)
                                .totalCount(chatService.getTotalCount())
                                .build()
                );
            }

            Thread.sleep(500); // 0.5초 대기
            attempt++;
        }

        // 30초 동안 새 메시지가 없으면 빈 응답 반환
        return ResponseEntity.ok(
                ChatDto.MessagesResponse.builder()
                        .chats(List.of())
                        .totalCount(chatService.getTotalCount())
                        .build()
        );
    }

    @GetMapping("")
    public String mainPage () {
        return "main";
    }

    @GetMapping("/refresh")
    public String refresh () {
        return "chat/refresh";
    }

    @GetMapping("/polling")
    public String polling () {
        return "chat/polling";
    }

    @GetMapping("/long-polling")
    public String longPolling () {
        return "chat/long_polling";
    }

    @GetMapping("/sse")
    public String sse () {
        return "chat/sse";
    }

    @GetMapping("/websocket")
    public String websocket () {
        return "chat/websocket";
    }
}
