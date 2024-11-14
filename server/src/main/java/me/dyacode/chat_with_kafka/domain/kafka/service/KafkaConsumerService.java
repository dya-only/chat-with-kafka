package me.dyacode.chat_with_kafka.domain.kafka.service;

import lombok.RequiredArgsConstructor;
import me.dyacode.chat_with_kafka.domain.kafka.dto.MessageDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "test", groupId = "chat")
    public void listen(MessageDto.Request message) {

        System.out.println("Received Message: " + message.getContent());
        messagingTemplate.convertAndSend("/topic/public", message);
    }
}
