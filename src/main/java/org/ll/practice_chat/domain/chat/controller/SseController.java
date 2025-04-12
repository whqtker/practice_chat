package org.ll.practice_chat.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.ll.practice_chat.domain.chat.SseEmitters;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Controller
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {
    // SSE 연결들을 관리하는 컴포넌트
    private final SseEmitters sseEmitters;

    // 클라이언트의 SSE 연결 요청을 처리하는 엔드포인트
    // /sse/connect로 GET 요청이 오면 SSE 스트림을 생성
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        // 새로운 SSE 연결 생성 (기본 타임아웃 30초)
        SseEmitter emitter = new SseEmitter();

        // 생성된 emitter를 컬렉션에 추가하여 관리
        sseEmitters.add(emitter);

        try {
            // 연결된 클라이언트에게 초기 연결 성공 메시지 전송
            emitter.send(SseEmitter.event()
                    .name("connect")    // 이벤트 이름을 "connect"로 지정
                    .data("connected!")); // 전송할 데이터
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }
}
