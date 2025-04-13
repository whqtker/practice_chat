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
    public ResponseEntity<ChatDto.WriteMessageResponse> writeMessageSse(@RequestBody ChatDto.WriteMessageRequest writeMessageRequest) {
        Chat chat = chatService.writeMessageSse(writeMessageRequest.getName(), writeMessageRequest.getContent());

        return ResponseEntity.ok(
                ChatDto.WriteMessageResponse.builder()
                        .chat(chat)
                        .build()
        );
    }

    @PostMapping("/write-websocket")
    @ResponseBody
    public ResponseEntity<ChatDto.WriteMessageResponse> writeMessageWebSocket(@RequestBody ChatDto.WriteMessageRequest writeMessageRequest) {
        Chat chat = chatService.writeMessageWebSocket(writeMessageRequest.getName(), writeMessageRequest.getContent());

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
        List<Chat> chats;

        while (true) {
            chats = chatService.getMessages(fromId);

            if (!chats.isEmpty()) {
                return ResponseEntity.ok(
                        ChatDto.MessagesResponse.builder()
                                .chats(chats)
                                .totalCount(chatService.getTotalCount())
                                .build()
                );
            }

            Thread.sleep(500);
        }
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
