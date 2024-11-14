package me.dyacode.chat_with_kafka.domain.kafka.controller;

import lombok.RequiredArgsConstructor;
import me.dyacode.chat_with_kafka.domain.kafka.dto.MessageDto;
import me.dyacode.chat_with_kafka.domain.kafka.service.KafkaProducerService;
import me.dyacode.chat_with_kafka.domain.kafka.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducerService producerService;
    private final MessageService messageService;

//    @PostMapping("/send")
//    public String produceMessage(@RequestBody MessageDto messageDto) {
//        producerService.sendMessage(messageDto);
//        return messageDto.getSender() + "가 메시지를 보냈습니다: " + messageDto.getContent();
//    }

    @MessageMapping("/chat")
//    @SendTo("/topic/public")
    public MessageDto.Response sendMessage(MessageDto.Request message) {

        producerService.sendMessage(message);
        return messageService.saveMessage(message);
    }
}