package me.dyacode.chat_with_kafka.domain.kafka.repository;

import me.dyacode.chat_with_kafka.domain.kafka.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop20ByOrderByCreatedAtDesc();
}
