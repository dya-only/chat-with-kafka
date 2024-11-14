package me.dyacode.chat_with_kafka.domain.kafka.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.dyacode.chat_with_kafka.domain.kafka.dto.MessageDto;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
public class Message {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String sender;
    private Integer imageType;
    private LocalDateTime createdAt;

    public Message (MessageDto.Request messageDto) {
        this.content = messageDto.getContent();
        this.sender = messageDto.getSender();
        this.imageType = messageDto.getImageType();
        this.createdAt = LocalDateTime.now();
    }
}