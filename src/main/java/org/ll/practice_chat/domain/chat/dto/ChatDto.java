package org.ll.practice_chat.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import org.ll.practice_chat.domain.chat.entity.Chat;

import java.util.List;

public class ChatDto {
    @Getter
    @Builder
    public static class WriteMessageRequest {
        private String name;
        private String content;
    }

    @Getter
    @Builder
    public static class WriteMessageResponse {
        private Chat chat;
    }

    @Getter
    @Builder
    public static class MessagesResponse {
        private List<Chat> chats;
        private int totalCount;
    }
}
