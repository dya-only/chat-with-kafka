package me.dyacode.chat_with_kafka.domain.kafka.service;

import lombok.RequiredArgsConstructor;
import me.dyacode.chat_with_kafka.domain.kafka.dto.MessageDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "test";
    private final KafkaTemplate<String, MessageDto.Request> kafkaTemplate;

    public void sendMessage(MessageDto.Request messageDto) {

        kafkaTemplate.send(TOPIC, messageDto);
        System.out.println(messageDto.getSender() + "가 메시지를 보냈습니다: " + messageDto.getContent());
    }
}
