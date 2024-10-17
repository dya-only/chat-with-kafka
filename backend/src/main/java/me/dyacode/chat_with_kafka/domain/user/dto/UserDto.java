package me.dyacode.chat_with_kafka.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.dyacode.chat_with_kafka.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public class UserDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String email;
        private String username;
        private String password;
        private MultipartFile image;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String email;
        private String username;
        private String imagePath;

        public Response(User user) {
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.imagePath = user.getImagePath();
        }
    }
}