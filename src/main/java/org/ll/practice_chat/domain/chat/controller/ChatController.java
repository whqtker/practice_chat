package org.ll.practice_chat.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.ll.practice_chat.domain.chat.dto.ChatDto;
import org.ll.practice_chat.domain.chat.entity.Chat;
import org.ll.practice_chat.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
}
