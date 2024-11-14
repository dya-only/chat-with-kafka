package me.dyacode.chat_with_kafka.domain.kafka.controller;

import lombok.RequiredArgsConstructor;
import me.dyacode.chat_with_kafka.domain.kafka.dto.MessageDto;
import me.dyacode.chat_with_kafka.domain.kafka.service.MessageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8090")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/messages/recent")
    public List<MessageDto.Response> getRecentMessages() {
        return messageService.getRecentMessages();
    }
}
