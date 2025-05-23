package me.dyacode.chat_with_kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ChatWithKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatWithKafkaApplication.class, args);
	}
}