package me.dyacode.chat_with_kafka.domain.kafka.service;

import lombok.RequiredArgsConstructor;
import me.dyacode.chat_with_kafka.domain.kafka.dto.MessageDto;
import me.dyacode.chat_with_kafka.domain.kafka.entity.Message;
import me.dyacode.chat_with_kafka.domain.kafka.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageDto.Response saveMessage(MessageDto.Request request) {
        Message message = new Message(request);
        Message savedMessage = messageRepository.save(message);
        return new MessageDto.Response(savedMessage);
    }

    public List<MessageDto.Response> getRecentMessages() {
        List<Message> messages = messageRepository.findTop20ByOrderByCreatedAtDesc();
        return messages.stream()
                .sorted((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                .map(MessageDto.Response::new)
                .collect(Collectors.toList());
    }
}
