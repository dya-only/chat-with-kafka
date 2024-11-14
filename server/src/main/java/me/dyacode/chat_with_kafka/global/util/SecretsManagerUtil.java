package me.dyacode.chat_with_kafka.global.util;

import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class SecretsManagerUtil {

    private final SecretsManagerClient client;
    private final ObjectMapper objectMapper;

    public SecretsManagerUtil(SecretsManagerClient client) {
        this.client = client;
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, String> getSecret(String secretName) {
        GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        String secretString = client.getSecretValue(request).secretString();
        try {
            return objectMapper.readValue(secretString, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse secret JSON", e);
        }
    }
}
