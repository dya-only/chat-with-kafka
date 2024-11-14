package me.dyacode.chat_with_kafka.global.util;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private String originalImageName;
    private String storeImageName;
    private String storePath;
}