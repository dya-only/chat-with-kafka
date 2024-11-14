package me.dyacode.chat_with_kafka.domain.user.repository;

import me.dyacode.chat_with_kafka.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}