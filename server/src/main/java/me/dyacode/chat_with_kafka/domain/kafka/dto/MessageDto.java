package me.dyacode.chat_with_kafka.domain.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.dyacode.chat_with_kafka.domain.kafka.entity.Message;

import java.time.LocalDateTime;

public class MessageDto {

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class Request {
        String content;
        String sender;
        Integer imageType;
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        String content;
        String sender;
        Integer imageType;
        LocalDateTime createdAt;

        public Response (Message message) {
            this.content = message.getContent();
            this.sender = message.getSender();
            this.imageType = message.getImageType();
            this.createdAt = message.getCreatedAt();
        }
    }
}
