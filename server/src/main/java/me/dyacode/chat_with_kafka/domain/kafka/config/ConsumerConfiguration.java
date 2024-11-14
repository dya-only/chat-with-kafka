package me.dyacode.chat_with_kafka.domain.kafka.config;

import me.dyacode.chat_with_kafka.domain.kafka.dto.MessageDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfiguration {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageDto.Request> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageDto.Request> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MessageDto.Request> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092, kafka2:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "chat");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // Delegate Deserializers 설정
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        // JsonDeserializer 설정
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "me.dyacode.chat_with_kafka.domain.kafka.dto");
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "me.dyacode.chat_with_kafka.domain.kafka.dto.MessageDto.Request");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false); // 필요에 따라 설정

        return new DefaultKafkaConsumerFactory<>(configProps);
    }
}
